package jp.thelow.thelowSql;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

import jp.thelow.thelowSql.annotation.Table;
import jp.thelow.thelowSql.database.dao.ThelowDao;
import jp.thelow.thelowSql.database.logic.DataBaseExecutor;
import jp.thelow.thelowSql.database.logic.DataBaseRunnable;
import jp.thelow.thelowSql.database.logic.ExecuteUpdateRunner;
import jp.thelow.thelowSql.database.logic.SelectRunner;
import jp.thelow.thelowSql.database.logic.UpdateListRunner;
import jp.thelow.thelowSql.database.logic.UpdateRunner;
import jp.thelow.thelowSql.database.result.SelectResult;
import jp.thelow.thelowSql.database.result.UpdateResult;
import lombok.Getter;

public class DataBaseDataStore<T> implements DataStore<T> {

  public static ConcurrentHashMap<String, DataBaseExecutor> threadList = new ConcurrentHashMap<>();

  /** Entityの型 */
  @Getter
  Class<T> clazz;

  private ThelowDao thelowDao;

  /** タスクを示すキュー */
  private static ConcurrentHashMap<String, BlockingQueue<DataBaseRunnable>> taskQueueMap = new ConcurrentHashMap<>();

  DataBaseDataStore(Class<T> clazz) {
    thelowDao = new ThelowDao(clazz);
    this.clazz = clazz;
  }

  @Override
  public void getOneData(String whereQuery, Object[] params, Consumer<SelectResult<T>> consume) {
    addTask(new SelectRunner<>(() -> thelowDao.selectOne(whereQuery, params), consume));
  }

  @Override
  public void getMultiData(String whereQuery, Object[] params, Consumer<SelectResult<List<T>>> consume) {
    addTask(new SelectRunner<>(() -> thelowDao.selectList(whereQuery, params), consume));

  }

  @Override
  public void update(T bean, String whereQuery, Object[] params, Consumer<UpdateResult<T>> consume) {
    addTask(new UpdateRunner<>((() -> thelowDao.updateAll(bean, whereQuery, params)), consume, bean));
  }

  @Override
  public void insert(T bean, Consumer<UpdateResult<T>> consume) {
    addTask(new UpdateRunner<>(() -> thelowDao.insertAll(bean), consume, bean));
  }

  /**
   * タスクを追加する
   *
   * @param runner
   */
  public void addTask(DataBaseRunnable runner) {
    Table tableAnnotation = clazz.getAnnotation(Table.class);
    if (tableAnnotation == null) { throw new IllegalStateException("Entityにアノテーションが付与されていません。:" + clazz.getName()); }
    String key = (tableAnnotation.value() + "@" + tableAnnotation.threadName()).toUpperCase();
    BlockingQueue<DataBaseRunnable> queue = taskQueueMap.computeIfAbsent(key, k -> new PriorityBlockingQueue<>(20,
        (t1, t2) -> t1.isSelect() ? 1 : -1));
    queue.add(runner);

    threadList.computeIfAbsent(key, k -> {
      //次のタスクを取得するスレッドを作成する
      DataBaseExecutor thread = new DataBaseExecutor(key, () -> taskQueueMap.get(key).poll(30, TimeUnit.SECONDS));
      thread.start();
      return thread;
    });
  }

  @Override
  public void updateInsert(List<T> bean, Consumer<List<UpdateResult<T>>> consume) {
    addTask(new UpdateListRunner<>(d -> thelowDao.updateInsert(d), consume, bean));
  }

  @Override
  public void updateInsert(T bean, Consumer<UpdateResult<T>> consumer) {
    addTask(new UpdateRunner<>(() -> thelowDao.updateInsert(bean), consumer, bean));
  }

  @Override
  public void execute(String query, Object[] params, IntConsumer callback) {
    addTask(new ExecuteUpdateRunner(() -> thelowDao.executeUpdate(query, params), callback));
  }

  public static long getTaskCount() {
    return taskQueueMap.values().stream().mapToLong(e -> e.size()).sum();
  }

  public static String getTaskStatus() {
    return taskQueueMap.entrySet().stream().map(e -> e.getKey() + "->" + e.getValue().size())
        .collect(Collectors.joining(", "));
  }

}

package jp.thelow.thelowSql;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import jp.thelow.thelowSql.database.logic.DataBaseExecutor;
import jp.thelow.thelowSql.database.logic.DataBaseRunner;
import lombok.Getter;

public class DataBaseDataStore<T> implements DataStore<T> {

  static ForkJoinPool forkJoinPool = new ForkJoinPool();

  /** 実行するタスク */
  private DataBaseExecutor<T> thread;

  /** Entityの型 */
  @Getter
  Class<T> clazz;

  /** data1を参照するならTRUE */
  boolean useData1 = true;

  /** タスクを示すキュー */
  BlockingQueue<DataBaseRunner<T, ?>> taskQueue = new PriorityBlockingQueue<>(20, (t1, t2) -> t1.isSelect() ? 1 : -1);

  Object lock = new Object();

  DataBaseDataStore(Class<T> clazz) {
    this.clazz = clazz;
    thread = new DataBaseExecutor<>(this);
    forkJoinPool.invoke(thread);
  }

  @Override
  public void getOneData(String whereQuery, Object[] params, Consumer<T> consume) {
    addTask(new DataBaseRunner<>(d -> d.selectOne(whereQuery, params), consume, true));
  }

  @Override
  public void getMultiData(String whereQuery, Object[] params, Consumer<List<T>> consume) {
    addTask(new DataBaseRunner<>(d -> d.selectList(whereQuery, params), consume, true));

  }

  @Override
  public void update(T bean, String whereQuery, Object[] params, Consumer<T> consume) {
    addTask(new DataBaseRunner<>(d -> d.updateAll(bean, whereQuery, params), consume, false));
  }

  @Override
  public void insert(T bean, Consumer<T> consume) {
    addTask(new DataBaseRunner<>(d -> d.insertAll(bean), consume, false));
  }

  /**
   * タスクを追加する
   *
   * @param runner
   */
  public void addTask(DataBaseRunner<T, ?> runner) {
    taskQueue.add(runner);
  }

  /**
   * 次のタスクを取得する。
   *
   * @return
   * @throws InterruptedException
   */
  public DataBaseRunner<T, ?> getNextTask() throws InterruptedException {
    return taskQueue.poll(30, TimeUnit.SECONDS);
  }

  @Override
  public void updateInsert(List<T> bean, Consumer<List<T>> consume) {
    addTask(new DataBaseRunner<>(d -> d.updateInsert(bean), consume, false));
  }

  @Override
  public void updateInsert(T bean, Consumer<T> consumer) {
    addTask(new DataBaseRunner<>(d -> d.updateInsert(bean), consumer, false));
  }

}

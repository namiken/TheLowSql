package jp.thelow.thelowSql;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import jp.thelow.thelowSql.database.dao.ThelowDao;
import jp.thelow.thelowSql.database.logic.DataBaseExecutor;
import jp.thelow.thelowSql.database.logic.DataBaseRunnable;
import jp.thelow.thelowSql.database.logic.SelectRunner;
import jp.thelow.thelowSql.database.logic.UpdateListRunner;
import jp.thelow.thelowSql.database.logic.UpdateRunner;
import jp.thelow.thelowSql.database.result.SelectResult;
import jp.thelow.thelowSql.database.result.UpdateResult;
import lombok.Getter;

public class DataBaseDataStore<T> implements DataStore<T> {

  private static DataBaseExecutor thread = new DataBaseExecutor();
  static {
    thread.start();
  }

  /** Entityの型 */
  @Getter
  Class<T> clazz;

  private ThelowDao thelowDao;

  /** タスクを示すキュー */
  private static BlockingQueue<DataBaseRunnable> taskQueue = new PriorityBlockingQueue<>(20, (t1, t2) -> t1.isSelect() ? 1 : -1);

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
    taskQueue.add(runner);
  }

  /**
   * 次のタスクを取得する。
   *
   * @return
   * @throws InterruptedException
   * @throws SQLException
   */
  public static DataBaseRunnable getNextTask() throws InterruptedException, SQLException {
    DataBaseRunnable nextTask = taskQueue.poll(30, TimeUnit.SECONDS);
    return nextTask;
  }

  @Override
  public void updateInsert(List<T> bean, Consumer<List<UpdateResult<T>>> consume) {
    addTask(new UpdateListRunner<>(d -> thelowDao.updateInsert(d), consume, bean));
  }

  @Override
  public void updateInsert(T bean, Consumer<UpdateResult<T>> consumer) {
    addTask(new UpdateRunner<>(() -> thelowDao.updateInsert(bean), consumer, bean));
  }

}

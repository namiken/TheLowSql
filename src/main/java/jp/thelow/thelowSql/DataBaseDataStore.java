package jp.thelow.thelowSql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import jp.thelow.thelowSql.database.logic.DataBaseExecutor;
import jp.thelow.thelowSql.database.logic.DataBaseRunner;
import lombok.Getter;

public class DataBaseDataStore<T> implements DataStore<T> {

  DataBaseExecutor<T> thread;

  /** Entityの型 */
  @Getter
  Class<T> clazz;

  /** data1を参照するならTRUE */
  boolean useData1 = true;

  /** タスクリスト１ */
  List<DataBaseRunner<T, ?>> data1 = Collections.synchronizedList(new ArrayList<>());
  /** タスクリスト2 */
  List<DataBaseRunner<T, ?>> data2 = Collections.synchronizedList(new ArrayList<>());

  Object lock = new Object();

  DataBaseDataStore(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public void getOneData(String whereQuery, Object[] params, Consumer<T> consume) {
    addTask(new DataBaseRunner<>(d -> d.selectOne(whereQuery, params), consume));
  }

  @Override
  public void getMultiData(String whereQuery, Object[] params, Consumer<List<T>> consume) {
    addTask(new DataBaseRunner<>(d -> d.selectList(whereQuery, params), consume));

  }

  @Override
  public void update(T bean, String whereQuery, Object[] params, Consumer<T> consume) {
    addTask(new DataBaseRunner<>(d -> d.updateAll(bean, whereQuery, params), consume));
  }

  @Override
  public void insert(T bean, Consumer<T> consume) {
    addTask(new DataBaseRunner<>(d -> d.insertAll(bean), consume));
  }

  /**
   * タスクを追加する
   *
   * @param runner
   */
  public void addTask(DataBaseRunner<T, ?> runner) {
    synchronized (lock) {
      if (useData1) {
        if (data1 == null) {
          data1 = new ArrayList<>();
        }
        data1.add(runner);
      } else {
        if (data2 == null) {
          data2 = new ArrayList<>();
        }
        data2.add(runner);
      }
    }

    // Exectorが動いていないなら開始する
    if (thread == null || thread.isDead()) {
      thread = new DataBaseExecutor<>(this);
      thread.start();
    }
  }

  /**
   * 次のタスクを取得する。
   *
   * @return
   */
  public List<DataBaseRunner<T, ?>> getNextTaskList() {
    boolean useData1Local = useData1;
    useData1 = !useData1;

    List<DataBaseRunner<T, ?>> taskList;
    if (useData1Local) {
      taskList = data1;
      data1 = null;
    } else {
      taskList = data2;
      data2 = null;
    }

    return taskList;
  }

  /**
   * タスク件数が0かどうか取得する。
   *
   * @return
   */
  public int getTaskCount() {
    synchronized (lock) {
      int totalTask = 0;
      if (data1 != null) {
        totalTask += data1.size();
      }

      if (data2 != null) {
        totalTask += data2.size();
      }
      return totalTask;
    }
  }

  @Override
  public void updateInsert(List<T> bean, Consumer<List<T>> consume) {
    addTask(new DataBaseRunner<>(d -> d.updateInsert(bean), consume));
  }

  @Override
  public void updateInsert(T bean, Consumer<T> consumer) {
    addTask(new DataBaseRunner<>(d -> d.updateInsert(bean), consumer));
  }

}

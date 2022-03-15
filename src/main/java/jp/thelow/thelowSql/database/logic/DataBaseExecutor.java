package jp.thelow.thelowSql.database.logic;

import java.sql.SQLException;

import jp.thelow.thelowSql.Main;
import jp.thelow.thelowSql.database.ConnectionFactory;
import lombok.Setter;

public class DataBaseExecutor extends Thread {

  private static final int TIMEOUT = 30 * 1000;
  private CustomSupplier supplier;

  @Setter
  private boolean isStop;
  private String tableName;

  public DataBaseExecutor(String tableName, CustomSupplier supplier) {
    this.tableName = tableName;
    this.supplier = supplier;
  }

  @Override
  public void run() {
    // 実行中がチェックする
    if (!Main.processing.get()) { throw new IllegalStateException("プラグインが実行中ではありません。"); }

    while (true) {
      if (isStop) {
        Main.getPlugin().getLogger().info("Threadを終了します。:" + tableName);
        break;
      }

      try {
        DataBaseRunnable nextTask = supplier.getNextTask();
        if (nextTask != null) {
          // タスクを実行する
          nextTask.run();
        } else {
          // コネクションを切断する
          ConnectionFactory.safeClose();
        }

      } catch (InterruptedException e) {
        //何もしない
      } catch (SQLException e) {
        // 接続失敗
        e.printStackTrace();
        // 30秒スレッドを停止させる
        waitThread();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * スレッドを停止する
   */
  private void waitThread() {
    try {
      Thread.currentThread().wait(TIMEOUT);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
  }

  public interface CustomSupplier {
    DataBaseRunnable getNextTask() throws InterruptedException, SQLException;
  }
}

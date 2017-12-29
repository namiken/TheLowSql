package jp.thelow.thelowSql.database.logic;

import java.sql.SQLException;

import jp.thelow.thelowSql.DataBaseDataStore;
import jp.thelow.thelowSql.Main;
import jp.thelow.thelowSql.database.ConnectionFactory;

public class DataBaseExecutor extends Thread {

  private static final int TIMEOUT = 30 * 1000;

  // private Connection connect;

  public DataBaseExecutor() {
    // try {
    // connect = connect();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // Bukkit.broadcastMessage(ChatColor.RED + "データーベースとの接続に失敗しました。現在ステータスの保存は行われません。");
    // }
  }

  @Override
  public void run() {
    // 実行中がチェックする
    if (!Main.processing.get()) { throw new IllegalStateException("プラグインが実行中ではありません。"); }

    while (true) {
      try {
        DataBaseRunnable nextTask = DataBaseDataStore.getNextTask();
        if (nextTask != null) {
          // タスクを実行する
          nextTask.run();
        } else {
          // コネクションを切断する
          ConnectionFactory.safeClose();

          // プラグインが実行中でないなら終了する
          if (!Main.processing.get()) {
            break;
          }
        }

      } catch (InterruptedException e) {
        // 起こり得ない
        e.printStackTrace();
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

}

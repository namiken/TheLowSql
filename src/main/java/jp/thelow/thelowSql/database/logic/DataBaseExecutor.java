package jp.thelow.thelowSql.database.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.RecursiveAction;

import org.bukkit.Bukkit;

import com.google.common.util.concurrent.UncheckedExecutionException;

import jp.thelow.thelowSql.DataBaseDataStore;
import jp.thelow.thelowSql.Main;
import jp.thelow.thelowSql.ThelowSqlConfig;
import jp.thelow.thelowSql.database.dao.ThelowDao;
import net.md_5.bungee.api.ChatColor;

public class DataBaseExecutor<T> extends RecursiveAction {

  private static final long serialVersionUID = 9064850810793506214L;

  private static final int TIMEOUT = 30 * 1000;

  private DataBaseDataStore<T> dataBaseDataStore;
  private Connection connect;

  public DataBaseExecutor(DataBaseDataStore<T> DataBaseDataStore) {
    dataBaseDataStore = DataBaseDataStore;
    try {
      connect = connect();
    } catch (SQLException e) {
      e.printStackTrace();
      Bukkit.broadcastMessage(ChatColor.RED + "データーベースとの接続に失敗しました。現在ステータスの保存は行われません。");
    }
  }

  /**
   * DBに接続
   */
  public static Connection connect() throws SQLException {
    ThelowSqlConfig config = Main.getTheLowSqlConfig();

    try {
      Class.forName("com.mysql.jdbc.Driver");
      return DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
    } catch (ClassNotFoundException ex) {
      // 起こりえない
      throw new UncheckedExecutionException(ex);
    }
  }

  @Override
  public void compute() {
    // 実行中がチェックする
    if (!Main.processing.get()) { throw new IllegalStateException("プラグインが実行中ではありません。"); }

    Connection connection = null;

    ThelowDao<T> thelowDao = new ThelowDao<>(dataBaseDataStore.getClazz());

    while (true) {
      try {
        DataBaseRunner<T, ?> nextTask = dataBaseDataStore.getNextTask();
        // タスクが存在する場合
        if (nextTask != null) {
          // DBと接続を行う
          if (connection == null || connection.isClosed()) {
            connection = connect();
          }
          // コネクションを最新のものに置き換える
          thelowDao.setCon(connection);
          // タスクを実行する
          nextTask.accept(thelowDao);
        } else {
          // 指定時間以上立った場合はクローズする
          if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
          }

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

  public boolean isDead() {
    return connect == null;
  }

}

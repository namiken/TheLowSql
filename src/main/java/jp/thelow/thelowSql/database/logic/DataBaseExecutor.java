package jp.thelow.thelowSql.database.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.Bukkit;

import com.google.common.util.concurrent.UncheckedExecutionException;

import jp.thelow.thelowSql.DataBaseDataStore;
import jp.thelow.thelowSql.Main;
import jp.thelow.thelowSql.ThelowSqlConfig;
import jp.thelow.thelowSql.database.dao.ThelowDao;
import net.md_5.bungee.api.ChatColor;

public class DataBaseExecutor<T> extends Thread {

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
  public void run() {
    if (connect != null) {
      ThelowDao<T> thelowDao = new ThelowDao<>(connect, dataBaseDataStore.getClazz());

      List<DataBaseRunner<T, ?>> nextTaskList = dataBaseDataStore.getNextTaskList();
      for (DataBaseRunner<T, ?> dataBaseRunner : nextTaskList) {
        dataBaseRunner.accept(thelowDao);
      }
    }

    if (dataBaseDataStore.getTaskCount() != 0 && connect != null) {
      run();
      return;
    }

    if (connect != null) {
      try {
        connect.close();
      } catch (SQLException e) {
        e.printStackTrace();
        // 一旦無視
      } finally {
        connect = null;
      }
    }
  }

  public boolean isDead() {
    return connect == null;
  }
}

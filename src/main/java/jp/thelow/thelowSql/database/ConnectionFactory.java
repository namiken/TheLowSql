package jp.thelow.thelowSql.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.common.util.concurrent.UncheckedExecutionException;

import jp.thelow.thelowSql.Main;
import jp.thelow.thelowSql.ThelowSqlConfig;
import jp.thelow.thelowSql.exception.UnchekedSqlException;

/**
 * コネクションの取得、破棄を担うクラス。
 */
public class ConnectionFactory {

  private static Connection connection = null;

  /**
   * 新しいコネクションを作成する。ここで作成されたコネクションは保存されません。
   */
  public static Connection connect() throws UnchekedSqlException {
    ThelowSqlConfig config = Main.getTheLowSqlConfig();

    try {
      Class.forName("com.mysql.jdbc.Driver");
      return DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
    } catch (ClassNotFoundException ex) {
      // 起こりえない
      throw new UncheckedExecutionException(ex);
    } catch (SQLException ex) {
      throw new UnchekedSqlException(ex);
    }
  }

  /**
   * コネクションを再接続する
   */
  public static void reconnect() {
    connection = connect();
  }

  /**
   * コネクションが切れていたらTRUE
   *
   * @return
   */
  public static boolean isDead() {
    try {
      return connection == null || connection.isClosed();
    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }
  }

  /**
   * コネクションを安全に切断する。
   */
  public static void safeClose() {
    if (!isDead()) {
      try {
        connection.close();
        connection = null;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * コネクションを取得する。状態によっては閉じている場合やnullの場合もあります。
   *
   * @return コネクション
   */
  public static Connection getConnection() {
    return connection;
  }

  /**
   * 安全に開いた状態のコネクションを取得する。
   *
   * @return コネクション
   * @throws SQLException SQLエラーが発生した場合
   */
  public static Connection getSafeConnection() {
    if (isDead()) {
      connection = connect();
    }
    return connection;
  }

}

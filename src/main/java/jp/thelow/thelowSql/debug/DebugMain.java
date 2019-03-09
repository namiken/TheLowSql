package jp.thelow.thelowSql.debug;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jp.thelow.thelowSql.database.ConnectionFactory;

public class DebugMain {
  private static String address = "localhost";
  private static String database = "thelow_data";
  private static String port = "3306";
  private static String user = "root";
  private static String password = "root";
  // private static String address = "eximradar.jp";
  // private static String database = "thelow_data";
  // private static String port = "3306";
  // private static String user = "root";
  // private static String password = "MCSV3ixSQLr9@";

  public static void main(String[] args) throws SQLException {
    ConfigHack.hackConfig(address, database, port, user, password);
    try (Connection connect = ConnectionFactory.connect()) {
      PreparedStatement statement = connect.prepareStatement("select * from HORSE_DATA where  PLAYER_ID = ?");
      statement.setString(1, "67d7d0a0-2e5a-498c-b74b-ea72e0b10b3d");
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        System.out.println(resultSet.getString("HORSE_ID"));
      }
    }
  }
}

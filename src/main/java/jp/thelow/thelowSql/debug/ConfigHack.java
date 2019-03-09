package jp.thelow.thelowSql.debug;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import jp.thelow.thelowSql.Main;
import jp.thelow.thelowSql.ThelowSqlConfig;

public class ConfigHack {

  public static ThelowSqlConfig hackConfig(String address, String database, String port, String user, String password) {
    ThelowSqlConfig config = new ThelowSqlConfig();
    config.load(new HackFileConfiguration(address, database, port, user, password));
    Main.config = config;
    return config;
  }

  private static class HackFileConfiguration extends FileConfiguration {

    private String address;
    private String database;
    private String port;
    private String user;
    private String password;

    public HackFileConfiguration(String address, String database, String port, String user, String password) {
      this.address = address;
      this.database = database;
      this.port = port;
      this.user = user;
      this.password = password;
    }

    @Override
    protected String buildHeader() {
      return null;
    }

    @Override
    public void loadFromString(String arg0) throws InvalidConfigurationException {}

    @Override
    public String saveToString() {
      return null;
    }

    @Override
    public String getString(String path) {
      switch (path) {
        case "database.host":
          return address;
        case "database.port":
          return port;
        case "database.user":
          return user;
        case "database.password":
          return password;
        case "database.database":
          return database;
        default:
          break;
      }
      return super.getString(path);
    }
  }
}

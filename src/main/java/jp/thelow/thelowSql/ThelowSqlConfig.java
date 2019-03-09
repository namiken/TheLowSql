package jp.thelow.thelowSql;

import org.bukkit.configuration.file.FileConfiguration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThelowSqlConfig {

  private String url;
  private String user;
  private String password;

  public void load(FileConfiguration config) {
    url = "jdbc:mysql://" + config.getString("database.host") + ":" + config.getString("database.port") + "/" +
        config.getString("database.database");
    user = config.getString("database.user");
    password = config.getString("database.password");
  }
}

package jp.thelow.thelowSql;

import org.bukkit.plugin.java.JavaPlugin;

import jp.thelow.thelowSql.command.TestConnection;

public class Main extends JavaPlugin {

  private static JavaPlugin plugin = null;

  public static ThelowSqlConfig config = null;

  @Override
  public void onEnable() {
    plugin = this;

    this.getConfig().options().copyDefaults(true);

    config = new ThelowSqlConfig();
    config.load(getConfig());

    getCommand("test_connect").setExecutor(new TestConnection());
  }

  public static JavaPlugin getPlugin() {
    return plugin;
  }

  public static ThelowSqlConfig getTheLowSqlConfig() {
    return config;
  }
}

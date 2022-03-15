package jp.thelow.thelowSql;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.plugin.java.JavaPlugin;

import jp.thelow.thelowSql.command.TestConnection;

public class Main extends JavaPlugin {

  private static JavaPlugin plugin = null;

  public static ThelowSqlConfig config = null;

  /** プラグインが動いていたらTRUE */
  public static final AtomicBoolean processing = new AtomicBoolean(true);

  @Override
  public void onEnable() {
    plugin = this;

    this.getConfig().options().copyDefaults(true);

    config = new ThelowSqlConfig();
    config.load(getConfig());

    getConfig().options().copyDefaults(true);
    saveDefaultConfig();

    getCommand("test_connect").setExecutor(new TestConnection());
    processing.set(true);
  }

  @Override
  public void onDisable() {
    super.onDisable();
    processing.set(false);

    getLogger().info("SQLタスクの完了を待ちます。");
    int count = 0;
    while (true) {
      long taskCount = DataBaseDataStore.getTaskCount();
      if (taskCount == 0) {
        getLogger().info("すべてのSQLタスクが完了しました。");
        break;
      }

      if (count % 1000 == 0) {
        getLogger().info("タスクの終了待ちです。:" + DataBaseDataStore.getTaskStatus() + ", 合計:" + taskCount);
      }

      if (count > 5000) {
        getLogger().info("一部のSQLタスクが完了しませんでした。:" + DataBaseDataStore.getTaskStatus());
        break;
      }

      try {
        Thread.sleep(50);
        count += 50;
      } catch (InterruptedException e) {
        //何もしない
        break;
      }
    }

    getLogger().info("すべてのスレッドを停止します:" + DataBaseDataStore.threadList.size());
    DataBaseDataStore.threadList.forEach((k, v) -> {
      v.interrupt();
      v.setStop(true);
    });
  }

  public static JavaPlugin getPlugin() {
    return plugin;
  }

  public static ThelowSqlConfig getTheLowSqlConfig() {
    return config;
  }
}

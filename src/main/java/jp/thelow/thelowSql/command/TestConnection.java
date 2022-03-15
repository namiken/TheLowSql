package jp.thelow.thelowSql.command;

import java.sql.Connection;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import jp.thelow.thelowSql.DataBaseDataStore;
import jp.thelow.thelowSql.database.ConnectionFactory;
import net.md_5.bungee.api.ChatColor;

public class TestConnection implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command paramCommand, String paramString,
      String[] paramArrayOfString) {
    try (Connection connect = ConnectionFactory.connect()) {
      sender.sendMessage(ChatColor.GREEN + "データベースとの接続に成功しました。");
    } catch (Exception e) {
      sender.sendMessage(ChatColor.RED + "データベースとの接続に失敗しました。");
    }

    sender.sendMessage("==Task情報==");
    sender.sendMessage(DataBaseDataStore.getTaskStatus());
    return true;
  }

}

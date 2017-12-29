package jp.thelow.thelowSql.command;

import java.sql.Connection;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import jp.thelow.thelowSql.database.ConnectionFactory;
import net.md_5.bungee.api.ChatColor;

public class TestConnection implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
    try (Connection connect = ConnectionFactory.connect()) {
      paramCommandSender.sendMessage(ChatColor.GREEN + "データベースとの接続に成功しました。");
    } catch (Exception e) {
      paramCommandSender.sendMessage(ChatColor.RED + "データベースとの接続に失敗しました。");
    }
    return true;
  }

}

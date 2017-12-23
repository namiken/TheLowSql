package jp.thelow.thelowSql.util;

import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;

/**
 * TheLowSqlの大部分で使用されるロガーです。
 *
 * @version 2.0
 * @see LogLevel
 */
public final class SqlLogger {

  /**
   * ロガーで使用可能なログのレベルを表します。
   *
   * @since 2.0
   * @version 2.0
   * @author azure
   * @see DungeonLogger
   */
  public static enum LogLevel {
    /** 情報メッセージ */
    INFO,
    /** 警告メッセージ */
    WARNING,
    /** エラーメッセージ */
    ERROR,
    /** SEVEREメッセージ */
    SEVERE,
    /** デバッグ情報メッセージ */
    DEBUG,
    /** 開発情報メッセージ */
    DEVELOPMENT,
    /** トレースレベルのメッセージ */
    TRACE;
  }

  /**
   * <p>
   * 指定されたログ レベルでメッセージのログをとります。
   * </p>
   *
   * @param message 文字列のメッセージ
   * @param level ログのレベル
   */
  public static void log(String message, LogLevel level) {
    if (Bukkit.getServer() == null) { return; }

    StringBuilder prefix = new StringBuilder("[The Low SQL");
    Logger logger = Bukkit.getLogger();
    switch (level) {
      case INFO:
        prefix.append(" Info] ");
        logger.info(prefix.append(message).toString());
        break;
      case WARNING:
        prefix.append(" Warning] ");
        logger.warning(prefix.append(message).toString());
        break;
      case ERROR:
        prefix.append(" Error] ");
        logger.severe(prefix.append(message).toString());
        break;
      case SEVERE:
        prefix.append(" Severe] ");
        logger.severe(prefix.append(message).toString());
        break;
      case DEVELOPMENT:
        prefix.append(" Dev] ");
        logger.info(prefix.append(message).toString());
        break;
      case DEBUG:
        prefix.append(" Debug] ");
        logger.info(prefix.append(message).toString());
        break;
      case TRACE:
        prefix.append(" Trace] ");
        logger.info(prefix.append(message).toString());
        break;
      default:
        prefix.append("] ");
        logger.info(prefix.append(message).toString());
        break;
    }
  }

  /**
   * <p>
   * 情報メッセージのログをとります。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void info(String message) {
    log(message, LogLevel.INFO);
  }

  /**
   * <p>
   * 警告メッセージのログをとります。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void warning(String message) {
    log(message, LogLevel.WARNING);
  }

  /**
   * <p>
   * エラーメッセージのログをとります。
   * </p>
   * <p>
   * これは、SEVEREメッセージと同等です。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void error(String message) {
    log(message, LogLevel.ERROR);
  }

  /**
   * <p>
   * SEVEREメッセージのログをとります。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void severe(String message) {
    log(message, LogLevel.SEVERE);
  }

  /**
   * <p>
   * デバッグ情報メッセージのログをとります。
   * </p>
   * <p>
   * これは、情報メッセージと同等です。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void debug(String message) {
    log(message, LogLevel.DEBUG);
  }

  /**
   * <p>
   * 開発情報メッセージのログをとります。
   * </p>
   * <p>
   * これは、情報メッセージと同等です。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void development(String message) {
    log(message, LogLevel.DEVELOPMENT);
  }

  // TODO インスタンスを分ける
  public static boolean enableTraceLog = false;

  /**
   * <p>
   * Traceメッセージのログをとります。
   * </p>
   * <p>
   * これは、情報メッセージと同等です。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void trace(String message) {
    if (enableTraceLog) {
      log(message, LogLevel.TRACE);
    }
  }

  /**
   * <p>
   * Traceメッセージのログをとります。
   * </p>
   * <p>
   * これは、情報メッセージと同等です。
   * </p>
   *
   * @param message 文字列のメッセージ
   */
  public static void trace(Object... message) {
    if (enableTraceLog) {
      log(StringUtils.join(message), LogLevel.TRACE);
    }
  }

  public static boolean isEnableTrace() {
    return enableTraceLog;
  }

}

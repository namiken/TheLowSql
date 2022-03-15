package jp.thelow.thelowSql.util;

import org.bukkit.Bukkit;

import jp.thelow.thelowSql.Main;

public class CommonUtil {

  public static boolean onServer = true;

  /**
   *
   * 2つの配列を結合させる
   *
   * @param array1
   * @param array2
   * @return
   */
  public static <T> T[] joinArray(T[] array1, T[] array2) {

    @SuppressWarnings("unchecked")
    T[] newArray = (T[]) new Object[array1.length + array2.length];

    System.arraycopy(array1, 0, newArray, 0, array1.length);
    System.arraycopy(array2, 0, newArray, array1.length, array2.length);

    return newArray;
  }

  /**
   * メインスレッドでrunnerを実行する。
   *
   * @param runner 実行対象
   */
  public static void callSyncMethod(Runnable runner) {
    if (onServer) {
      Bukkit.getScheduler().callSyncMethod(Main.getPlugin(), () -> {
        try {
          runner.run();
          return null;
        } catch (Throwable e) {
          e.printStackTrace();
          throw e;
        }
      });
    } else {
      runner.run();
    }
  }
}

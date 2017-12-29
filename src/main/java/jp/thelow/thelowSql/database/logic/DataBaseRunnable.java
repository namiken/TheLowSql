package jp.thelow.thelowSql.database.logic;

public interface DataBaseRunnable extends Runnable {
  /**
   * セレクトかどうか取得する。
   *
   * @return セレクトならTRUE
   */
  boolean isSelect();

}

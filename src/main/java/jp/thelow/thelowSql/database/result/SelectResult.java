package jp.thelow.thelowSql.database.result;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SelectResult<T> {
  /** Exceptionが発生した場合はtrue */
  private boolean error = false;

  /** Exceptionインスタンス */
  private Exception errorInstance = null;

  /** 結果 */
  @Setter
  private T result = null;

  public void setErrorInstance(Exception errorInstance) {
    this.errorInstance = errorInstance;
    this.error = true;
  }
}

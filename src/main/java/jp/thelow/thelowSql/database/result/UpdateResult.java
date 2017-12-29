package jp.thelow.thelowSql.database.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateResult<T> {
  /** Exceptionが発生した場合はtrue */
  private boolean error;

  /** Exceptionインスタンス */
  private Exception errorInstance;

  private int count;

  /** 更新に用いるEntity */
  private T entity;
}

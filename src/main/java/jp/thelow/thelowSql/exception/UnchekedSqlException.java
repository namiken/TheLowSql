package jp.thelow.thelowSql.exception;

import java.sql.SQLException;

import lombok.Getter;

@Getter
public class UnchekedSqlException extends RuntimeException {
  private static final long serialVersionUID = 1298936451094528334L;
  private SQLException sqlException;

  public UnchekedSqlException(SQLException sqlException) {
    super(sqlException);
    this.sqlException = sqlException;
  }

}

package jp.thelow.thelowSql.exception;

public class InvalidEntityException extends RuntimeException {

  private static final long serialVersionUID = 2587905879214488264L;
  private String msg;

  public InvalidEntityException(String msg) {
    super(msg);
    this.msg = msg;
  }

  public String getMessage() {
    return msg;
  }
}

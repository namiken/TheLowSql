package jp.thelow.thelowSql.database.logic;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import jp.thelow.thelowSql.exception.UnchekedSqlException;
import jp.thelow.thelowSql.util.CommonUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExecuteUpdateRunner implements DataBaseRunnable {

  private IntSupplier mainRunnable;

  private IntConsumer callback;

  @Override
  public void run() {
    try {
      int updateCount = mainRunnable.getAsInt();
      CommonUtil.callSyncMethod(() -> callback.accept(updateCount));
    } catch (UnchekedSqlException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean isSelect() {
    return false;
  }

}

package jp.thelow.thelowSql.database.logic;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

import jp.thelow.thelowSql.database.result.UpdateResult;
import jp.thelow.thelowSql.exception.UnchekedSqlException;
import jp.thelow.thelowSql.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateRunner<T> implements DataBaseRunnable {
  private IntSupplier mainRunnable;

  private Consumer<UpdateResult<T>> callback;

  private T entity;

  @Override
  public void run() {
    UpdateResult<T> result = new UpdateResult<>();
    result.setEntity(entity);
    try {
      int updateCount = mainRunnable.getAsInt();
      result.setCount(updateCount);
    } catch (UnchekedSqlException e) {
      e.printStackTrace();
      result.setErrorInstance(e.getSqlException());
    } catch (Exception e) {
      e.printStackTrace();
      result.setErrorInstance(e);
    }

    CommonUtil.callSyncMethod(() -> callback.accept(result));
  }

  @Override
  public boolean isSelect() {
    return false;
  }
}

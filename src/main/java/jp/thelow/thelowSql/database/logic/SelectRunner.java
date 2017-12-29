package jp.thelow.thelowSql.database.logic;

import java.util.function.Consumer;
import java.util.function.Supplier;

import jp.thelow.thelowSql.database.result.SelectResult;
import jp.thelow.thelowSql.exception.UnchekedSqlException;
import jp.thelow.thelowSql.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SelectRunner<X> implements DataBaseRunnable {
  private Supplier<X> mainRunnable;

  private Consumer<SelectResult<X>> callback;

  @Override
  public void run() {
    SelectResult<X> result = new SelectResult<>();

    try {
      X object = mainRunnable.get();
      // 結果を生成
      result.setResult(object);
    } catch (UnchekedSqlException e) {
      e.printStackTrace();
      result.setErrorInstance(e.getSqlException());
    } catch (Exception e) {
      e.printStackTrace();
      result.setErrorInstance(e);
    }
    // コールバック関数実行
    CommonUtil.callSyncMethod(() -> callback.accept(result));
  }

  @Override
  public boolean isSelect() {
    return true;
  }
}

package jp.thelow.thelowSql.database.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

import jp.thelow.thelowSql.database.result.UpdateResult;
import jp.thelow.thelowSql.exception.UnchekedSqlException;
import jp.thelow.thelowSql.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateListRunner<T> implements DataBaseRunnable {
  private ToIntFunction<T> mainRunnable;

  private Consumer<List<UpdateResult<T>>> callback;

  private List<T> entityList;

  @Override
  public void run() {
    List<UpdateResult<T>> resultList = new ArrayList<>();
    for (T entity : entityList) {
      UpdateResult<T> result = new UpdateResult<>();
      result.setEntity(entity);
      try {
        int updateCount = mainRunnable.applyAsInt(entity);
        result.setCount(updateCount);
      } catch (UnchekedSqlException e) {
        e.printStackTrace();
        result.setErrorInstance(e.getSqlException());
      } catch (Exception e) {
        e.printStackTrace();
        result.setErrorInstance(e);
      }
      resultList.add(result);
    }

    CommonUtil.callSyncMethod(() -> callback.accept(resultList));
  }

  @Override
  public boolean isSelect() {
    return false;
  }
}

package jp.thelow.thelowSql.database.logic;

import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.Bukkit;

import jp.thelow.thelowSql.Main;
import jp.thelow.thelowSql.database.dao.ThelowDao;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DataBaseRunner<T, X> implements Consumer<ThelowDao<T>> {
  private Function<ThelowDao<T>, X> mainRunnable;

  private Consumer<X> callback;

  @Override
  public void accept(ThelowDao<T> dao) {
    try {
      X object = mainRunnable.apply(dao);
      Bukkit.getScheduler().callSyncMethod(Main.getPlugin(), () -> {
        callback.accept(object);
        return null;
      });
    } catch (Exception e) {
      // TODO 一旦何もしない
      e.printStackTrace();
    }
  }
}

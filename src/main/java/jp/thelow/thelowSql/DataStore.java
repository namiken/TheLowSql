package jp.thelow.thelowSql;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import jp.thelow.thelowSql.database.result.SelectResult;
import jp.thelow.thelowSql.database.result.UpdateResult;

/**
 * データを保存・取得するためのクラス。
 */
public interface DataStore<T> {

  /**
   * 1つのデータを取得する
   *
   * @param whereQuery
   * @param params TODO
   */
  void getOneData(String whereQuery, Object[] params, Consumer<SelectResult<T>> consume);

  /**
   * 複数のデータを取得する
   *
   * @param id
   */
  void getMultiData(String whereQuery, Object[] params, Consumer<SelectResult<List<T>>> consume);

  /**
   * データを一件更新する
   *
   * @param bean
   * @param whereQuery TODO
   * @param params TODO
   */
  void update(T bean, String whereQuery, Object[] params, Consumer<UpdateResult<T>> consume);

  /**
   * データを一件挿入する
   *
   * @param bean
   */
  void insert(T bean, Consumer<UpdateResult<T>> consume);

  /**
   * 複数件のデータをupdate insert処理を行う。
   *
   * @param bean
   * @param whereQuery
   * @param params
   * @param consumer
   */
  void updateInsert(List<T> bean, Consumer<List<UpdateResult<T>>> consumer);

  /**
   * 1件のデータをupdate insert処理を行う。
   *
   * @param bean
   * @param whereQuery
   * @param params
   * @param consumer
   */
  void updateInsert(T bean, Consumer<UpdateResult<T>> consumer);

  /**
   * SQLを実行する。
   *
   * @param whereQuery
   * @param params TODO
   */
  void execute(String query, Object[] params, IntConsumer callback);
}

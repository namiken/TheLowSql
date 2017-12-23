package jp.thelow.thelowSql;

import java.util.List;
import java.util.function.Consumer;

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
  void getOneData(String whereQuery, Object[] params, Consumer<T> consume);

  /**
   * 複数のデータを取得する
   *
   * @param id
   */
  void getMultiData(String whereQuery, Object[] params, Consumer<List<T>> consume);

  /**
   * データを一件更新する
   *
   * @param bean
   * @param whereQuery TODO
   * @param params TODO
   */
  void update(T bean, String whereQuery, Object[] params, Consumer<T> consumer);

  /**
   * データを一件挿入する
   *
   * @param bean
   */
  void insert(T bean, Consumer<T> consumer);

  /**
   * 複数件のデータをupdate insert処理を行う。
   *
   * @param bean
   * @param whereQuery
   * @param params
   * @param consumer
   */
  void updateInsert(List<T> bean, Consumer<List<T>> consumer);

  /**
   * 1件のデータをupdate insert処理を行う。
   *
   * @param bean
   * @param whereQuery
   * @param params
   * @param consumer
   */
  void updateInsert(T bean, Consumer<T> consumer);

}

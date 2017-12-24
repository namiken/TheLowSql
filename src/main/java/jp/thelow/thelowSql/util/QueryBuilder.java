package jp.thelow.thelowSql.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jp.thelow.thelowSql.annotation.Column;
import jp.thelow.thelowSql.database.cacher.DataBaseDataMapper;
import jp.thelow.thelowSql.database.cacher.DataBaseDataMapper.DataBaseInfo;

public class QueryBuilder {

  String cacheUpdateQuery = null;
  String cacheInsertQuery = null;
  String cacheUpdateInsertQuery = null;
  String selectCache = null;

  private Class<?> clazz;

  public QueryBuilder(Class<?> clazz) {
    this.clazz = clazz;
  }

  /**
   * updateのQueryを作成する。
   *
   * @return
   */
  public String updateQuery() {
    if (cacheUpdateQuery == null) {
      DataBaseInfo dataBaseInfo = DataBaseDataMapper.getDataBaseInfo(clazz);

      StringBuilder query = new StringBuilder();
      query.append("update ");
      query.append(dataBaseInfo.getTableName());
      query.append(" set  ");

      Field[] declaredFields = clazz.getDeclaredFields();
      String setQuery = Arrays.stream(declaredFields).map(f -> f.getAnnotation(Column.class)).filter(a -> a != null).map(a -> a.value() + " = ? ")
          .collect(Collectors.joining(","));
      query.append(setQuery);
      query.append(" where ");
      cacheUpdateQuery = query.toString();
    }
    return cacheUpdateQuery;
  }

  /**
   * Entityからパラメータを取得する
   *
   * @param entity
   * @return
   */
  public Object[] params(Object entity) {
    try {
      ArrayList<Object> objectList = new ArrayList<>();

      for (Field field : clazz.getDeclaredFields()) {
        // カラムかどうか確認
        if (field.getAnnotation(Column.class) == null) {
          continue;
        }
        field.setAccessible(true);
        objectList.add(field.get(entity));
      }
      return objectList.toArray();
    } catch (IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
      // 起こりえない
      throw new RuntimeException(e);
    }
  }

  /**
   * insertのQueryを作成する。
   *
   * @return
   */
  public String insertQuery() {
    if (cacheInsertQuery == null) {
      DataBaseInfo dataBaseInfo = DataBaseDataMapper.getDataBaseInfo(clazz);

      StringBuilder query = new StringBuilder();
      query.append("insert into ");
      query.append(dataBaseInfo.getTableName());
      query.append(" (  ");

      Field[] declaredFields = clazz.getDeclaredFields();
      List<String> collect = Arrays.stream(declaredFields).map(f -> f.getAnnotation(Column.class)).filter(a -> a != null).map(a -> a.value())
          .collect(Collectors.toList());
      query.append(collect.stream().collect(Collectors.joining(",")));

      query.append(" ) values ( ");

      String value = Stream.generate(() -> "?").limit(collect.size()).collect(Collectors.joining(" ,"));
      query.append(value);
      query.append(" ) ");

      cacheInsertQuery = query.toString();
    }
    return cacheInsertQuery;
  }

  /**
   *
   * selectを取得する。
   *
   * @return
   */
  public String getSelect() {
    if (selectCache == null) {
      DataBaseInfo dataBaseInfo = DataBaseDataMapper.getDataBaseInfo(clazz);
      selectCache = "select * from " + dataBaseInfo.getTableName() + " where ";
    }
    return selectCache;
  }

  /**
   * update insert文を取得する。
   *
   * @param entity
   * @return
   */
  public String getUpdateInsert() {
    if (cacheUpdateInsertQuery != null) { return cacheUpdateInsertQuery; }

    StringBuilder query = new StringBuilder();

    // insert
    String insertQuery = insertQuery();
    query.append(insertQuery);
    query.append(" ON DUPLICATE KEY UPDATE ");

    // update
    Field[] declaredFields = clazz.getDeclaredFields();
    String setQuery = Arrays.stream(declaredFields).map(f -> f.getAnnotation(Column.class)).filter(a -> a != null).map(a -> a.value() + " = ? ")
        .collect(Collectors.joining(","));
    query.append(setQuery);

    cacheUpdateInsertQuery = query.toString();

    return query.toString();
  }

  /**
   * テーブル情報を取得する。
   *
   * @return テーブル情報
   */
  public DataBaseInfo getDataBaseInfo() {
    return DataBaseDataMapper.getDataBaseInfo(clazz);
  }
}

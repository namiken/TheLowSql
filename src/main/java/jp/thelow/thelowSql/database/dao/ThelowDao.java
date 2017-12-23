package jp.thelow.thelowSql.database.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import jp.thelow.thelowSql.exception.UnchekedSqlException;
import jp.thelow.thelowSql.util.CommonUtil;
import jp.thelow.thelowSql.util.QueryBuilder;
import jp.thelow.thelowSql.util.SqlLogger;

public class ThelowDao<T> {

  private static final BasicRowProcessor CONVERT = new BasicRowProcessor(new GenerousBeanProcessor());

  private Class<T> clazz;

  QueryBuilder queryBuilder;

  QueryRunner qr = new QueryRunner();

  private Connection con;

  public ThelowDao(Connection connection, Class<T> clazz) {
    this.con = connection;
    this.clazz = clazz;
    queryBuilder = new QueryBuilder(clazz);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public T selectOne(String whereQuery, Object[] params) {
    try {
      ResultSetHandler<T> handler = new BeanHandler(clazz, CONVERT);
      String sql = queryBuilder.getSelect() + whereQuery;

      SqlLogger.info("execute select:" + sql);
      return qr.query(con, sql, handler, params);

    } catch (SQLException e) {
      throw new UnchekedSqlException(e);
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public List<T> selectList(String whereQuery, Object[] params) {
    try {
      ResultSetHandler<T> handler = new BeanListHandler(clazz, CONVERT);

      String sql = queryBuilder.getSelect() + whereQuery;
      SqlLogger
          .info("execute select list:" + sql + ", params:" + Arrays.toString(params));
      return (List<T>) qr.query(con, sql, handler, params);
    } catch (SQLException e) {
      throw new UnchekedSqlException(e);
    }
  }

  /**
   * updateを行う
   *
   * @param updateQuery update句
   * @param setParam setに用いるパラメータ
   * @param whereQuery where句
   * @param whereParam whereに用いるパラメータ
   * @return 更新件数
   */
  public int update(String updateQuery, Object[] setParam, String whereQuery, Object[] whereParam) {
    try {
      String sql = updateQuery + " " + whereQuery;
      SqlLogger.info("execute update:" + sql);
      return qr.update(con, sql, CommonUtil.joinArray(setParam, whereParam));
    } catch (SQLException e) {
      throw new UnchekedSqlException(e);
    }
  }

  /**
   * Entityの内容をすべて更新を行う
   *
   * @param entity entity
   * @param whereQuery where句
   * @param whereParam where句に用いてるパラメータ
   * @return
   */
  public T updateAll(T entity, String whereQuery, Object[] whereParam) {
    try {
      String sql = queryBuilder.updateQuery() + " " + whereQuery;
      SqlLogger.info("execute update all:" + sql);
      int update = qr.update(con, sql, CommonUtil.joinArray(queryBuilder.params(entity), whereParam));
      if (update == 0) {
        return null;
      } else {
        return entity;
      }
    } catch (SQLException e) {
      throw new UnchekedSqlException(e);
    }
  }

  /**
   * Entityの内容を挿入する
   *
   * @param entity entity
   * @param whereQuery where句
   * @param whereParam where句に用いてるパラメータ
   * @return
   */
  public T insertAll(T entity) {
    try {
      String sql = queryBuilder.insertQuery();
      SqlLogger.info("execute insert:" + queryBuilder.getDataBaseInfo().getTableName());
      int insert = qr.update(con, sql, queryBuilder.params(entity));
      if (insert == 0) {
        return null;
      } else {
        return entity;
      }
    } catch (SQLException e) {
      throw new UnchekedSqlException(e);
    }
  }

  /**
   * Entityの内容をupdateInsertする
   *
   * @param entity entity
   * @param whereQuery where句
   * @param whereParam where句に用いてるパラメータ
   * @return
   */
  public List<T> updateInsert(List<T> entity) {
    String sql = queryBuilder.getUpdateInsert();

    // 更新されたデータ
    List<T> updatedList = new ArrayList<>();

    for (T t : entity) {
      try {
        int updateInsert = qr.update(con, sql, CommonUtil.joinArray(queryBuilder.params(t), queryBuilder.params(t)));
        SqlLogger.info(
            "execute update insert:" + queryBuilder.getDataBaseInfo().getTableName() + ", params:"
                + Arrays.toString(CommonUtil.joinArray(queryBuilder.params(t), queryBuilder.params(t))));

        if (updateInsert != 0) {
          updatedList.add(t);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return updatedList;
  }

  /**
   * Entityの内容をupdateInsertする
   *
   * @param entity entity
   * @param whereQuery where句
   * @param whereParam where句に用いてるパラメータ
   * @return
   */
  public T updateInsert(T entity) {
    String sql = queryBuilder.getUpdateInsert();

    try {
      qr.update(con, sql, CommonUtil.joinArray(queryBuilder.params(entity), queryBuilder.params(entity)));
      SqlLogger.info(
          "execute update insert:" + sql + ", params:"
              + Arrays.toString(CommonUtil.joinArray(queryBuilder.params(entity), queryBuilder.params(entity))));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return entity;
  }
}

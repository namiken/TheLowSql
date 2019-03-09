package jp.thelow.thelowSql.database.dao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import jp.thelow.thelowSql.database.ConnectionFactory;
import jp.thelow.thelowSql.exception.UnchekedSqlException;
import jp.thelow.thelowSql.util.CommonUtil;
import jp.thelow.thelowSql.util.QueryBuilder;
import jp.thelow.thelowSql.util.SqlLogger;

public class ThelowDao {

  private static final BasicRowProcessor CONVERT = new BasicRowProcessor(new GenerousBeanProcessor());

  private Class<?> clazz;

  QueryBuilder queryBuilder;

  QueryRunner qr = new QueryRunner();

  public ThelowDao(Class<?> clazz) {
    this.clazz = clazz;
    queryBuilder = new QueryBuilder(clazz);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public <T> T selectOne(String whereQuery, Object[] params) {
    try {
      ResultSetHandler<T> handler = new BeanHandler(clazz, CONVERT);
      String sql = queryBuilder.getSelect() + whereQuery;

      SqlLogger.info("execute select:" + sql);
      return qr.query(ConnectionFactory.getSafeConnection(), sql, handler, params);
    } catch (SQLException e) {
      throw new UnchekedSqlException(e);
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public <T> List<T> selectList(String whereQuery, Object[] params) {
    try {
      ResultSetHandler<T> handler = new BeanListHandler(clazz, CONVERT);

      String sql = queryBuilder.getSelect() + whereQuery;
      SqlLogger
          .info("execute select list:" + sql + ", params:" + Arrays.toString(params));
      return (List<T>) qr.query(ConnectionFactory.getSafeConnection(), sql, handler, params);
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
   * @throws SQLException
   */
  public int update(String updateQuery, Object[] setParam, String whereQuery, Object[] whereParam) {
    try {
      String sql = updateQuery + " " + whereQuery;
      SqlLogger.info("execute update:" + sql);
      return qr.update(ConnectionFactory.getSafeConnection(), sql, CommonUtil.joinArray(setParam, whereParam));
    } catch (SQLException e) {
      throw new UnchekedSqlException(e);
    }
  }

  /**
   * Entityの内容をすべて更新を行う
   *
   * @param <T>
   *
   * @param entity entity
   * @param whereQuery where句
   * @param whereParam where句に用いてるパラメータ
   * @return
   * @throws SQLException
   */
  public <T> int updateAll(T entity, String whereQuery, Object[] whereParam) {
    try {
      String sql = queryBuilder.updateQuery() + " " + whereQuery;
      SqlLogger.info("execute update all:" + sql);
      return qr.update(ConnectionFactory.getSafeConnection(), sql,
          CommonUtil.joinArray(queryBuilder.params(entity), whereParam));
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
   * @throws SQLException
   */
  public <T> int insertAll(T entity) {
    try {
      String sql = queryBuilder.insertQuery();
      SqlLogger.info("execute insert:" + queryBuilder.getDataBaseInfo().getTableName());
      return qr.update(ConnectionFactory.getSafeConnection(), sql, queryBuilder.params(entity));
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
  public <T> int updateInsert(T entity) {
    String sql = queryBuilder.getUpdateInsert();

    try {
      SqlLogger.info(
          "execute update insert:" + sql + "\r\n ・params:"
              + Arrays.toString(CommonUtil.joinArray(queryBuilder.params(entity), queryBuilder.params(entity))));
      return qr.update(ConnectionFactory.getSafeConnection(), sql,
          CommonUtil.joinArray(queryBuilder.params(entity), queryBuilder.params(entity)));
    } catch (SQLException e) {
      throw new UnchekedSqlException(e);
    }
  }

  /**
   * 1つのSQLを実行する。
   *
   * @param query
   * @param params
   * @return
   */
  public int executeUpdate(String query, Object[] params) {
    try {
      SqlLogger.info("execute query:" + query + "\r\n ・params:" + Arrays.toString(params));
      return qr.update(ConnectionFactory.getSafeConnection(), query, params);
    } catch (SQLException e) {
      throw new UnchekedSqlException(e);
    }
  }
}

package jp.thelow.thelowSql.database.test;

import jp.thelow.thelowSql.DataStore;
import jp.thelow.thelowSql.DataStoreFactory;
import jp.thelow.thelowSql.Main;
import jp.thelow.thelowSql.ThelowSqlConfig;
import jp.thelow.thelowSql.util.CommonUtil;

/**
 * テスト実行するためのクラス。
 */
public class DataBaseTest {
  public static void main(String[] args) {
    ThelowSqlConfig thelowSqlConfig = new ThelowSqlConfig();
    thelowSqlConfig.setUrl("jdbc:mysql://localhost:3306/thelow_data");
    thelowSqlConfig.setUser("root");
    thelowSqlConfig.setPassword("root");
    Main.config = thelowSqlConfig;

    CommonUtil.onServer = false;

    // selectTest();
    // insertTest();
    // updateTest();
    Main.processing.set(false);
  }

  public static void insertTest() {
    TestEntity testEntity = new TestEntity();
    testEntity.setUuid("326d0cd6-408b-4135-82a3-f2bd00248189");

    DataStore<TestEntity> dataStore = DataStoreFactory.getDataStore(TestEntity.class);
    dataStore.insert(testEntity, a -> {
      System.out.println(a.getCount());
    });
  }

  public static void updateTest() {

    TestEntity testEntity = new TestEntity();
    testEntity.setUuid("67d7d0a0-2e5a-498c-b74b-ea72e0b10b3d");
    testEntity.setBowExp(100);
    testEntity.setBowLevel(100);
    testEntity.setBowReincKeyList("1,2");

    DataStore<TestEntity> dataStore = DataStoreFactory.getDataStore(TestEntity.class);
    dataStore.update(testEntity, " uuid = a?", new Object[] { "67d7d0a0-2e5a-498c-b74b-ea72e0b10b3d" }, a -> {
      System.out.println(a.getCount());
      System.out.println(a.getErrorInstance());
    });
  }

  public static void selectTest() {

    DataStore<TestEntity> dataStore = DataStoreFactory.getDataStore(TestEntity.class);
    dataStore.getOneData(" uuid = ?", new Object[] { "01730d92-b140-4089-a980-deb75bad519b" }, a -> {
      System.out.println(a.isError());
      System.out.println(a.getErrorInstance());
      System.out.println(a.getResult());
    });
  }
}

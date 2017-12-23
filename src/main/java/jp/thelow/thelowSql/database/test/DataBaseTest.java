package jp.thelow.thelowSql.database.test;

import jp.thelow.thelowSql.DataStore;
import jp.thelow.thelowSql.DataStoreFactory;
import jp.thelow.thelowSql.Main;
import jp.thelow.thelowSql.ThelowSqlConfig;

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

    selectTest();
    // insertTest();
    // updateTest();
  }

  public static void insertTest() {
    TestEntity testEntity = new TestEntity();
    testEntity.setUuid("67d7d0a0-2e5a-498c-b74b-ea72e0b10b3d");

    DataStore<TestEntity> dataStore = DataStoreFactory.getDataStore(TestEntity.class);
    dataStore.insert(testEntity, a -> {});
  }

  public static void updateTest() {

    TestEntity testEntity = new TestEntity();
    testEntity.setUuid("67d7d0a0-2e5a-498c-b74b-ea72e0b10b3d");
    testEntity.setBowExp(100);
    testEntity.setBowLevel(100);
    testEntity.setBowReincKeyList("1,2");

    DataStore<TestEntity> dataStore = DataStoreFactory.getDataStore(TestEntity.class);
    dataStore.update(testEntity, " uuid = ?", new Object[] { "67d7d0a0-2e5a-498c-b74b-ea72e0b10b3d" }, a -> {});
  }

  public static void selectTest() {

    DataStore<TestEntity> dataStore = DataStoreFactory.getDataStore(TestEntity.class);
    dataStore.getOneData(" uuid = ?", new Object[] { "67d7d0a0-2e5a-498c-b74b-ea72e0b10b3d" }, a -> {});
  }
}

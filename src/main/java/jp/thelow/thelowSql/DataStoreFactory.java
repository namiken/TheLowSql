package jp.thelow.thelowSql;

import java.util.HashMap;

public class DataStoreFactory {
  private static HashMap<Class<?>, DataStore<?>> instanceMap = new HashMap<>();

  /**
   * DataStoreのインスタンスを取得する。
   *
   * @param clazz entity
   * @return
   */
  public static <T> DataStore<T> getDataStore(Class<T> clazz) {
    @SuppressWarnings("unchecked")
    DataStore<T> dataStore = (DataStore<T>) instanceMap.get(clazz);
    if (dataStore == null) {
      dataStore = new DataBaseDataStore<>(clazz);
      instanceMap.put(clazz, dataStore);
    }
    return dataStore;
  }
}

package jp.thelow.thelowSql.database.cacher;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import jp.thelow.thelowSql.annotation.Id;
import jp.thelow.thelowSql.annotation.Table;
import jp.thelow.thelowSql.exception.InvalidEntityException;
import lombok.Getter;
import lombok.Setter;

public class DataBaseDataMapper {

  /** DB 情報のマップ */
  private static ConcurrentHashMap<Class<?>, DataBaseInfo> dbInfoMap = new ConcurrentHashMap<>();

  /**
   * クラス名からDB情報を取得する
   *
   * @param clazz Entity
   * @return DB情報
   */
  public static DataBaseInfo getDataBaseInfo(Class<?> clazz) {
    // DBの情報を取得
    DataBaseInfo dataBaseInfo = dbInfoMap.get(clazz);
    if (dataBaseInfo != null) { return dataBaseInfo; }

    // 初期化
    dataBaseInfo = new DataBaseInfo();

    // Table名取得
    Table tableAnnotation = clazz.getAnnotation(Table.class);
    if (tableAnnotation == null) { throw new InvalidEntityException("Entity:" + clazz.getName() + "にTableアノテーションが指定されていません"); }
    String tableName = tableAnnotation.value();
    dataBaseInfo.setTableName(tableName);

    // ID名取得
    ArrayList<Field> idFieldList = new ArrayList<>();
    ArrayList<String> idColumnList = new ArrayList<>();
    for (Field field : clazz.getDeclaredFields()) {
      Id idAnnotation = field.getAnnotation(Id.class);
      // @idがないものは無視
      if (idAnnotation == null) {
        continue;
      }
      // フィールドを追加
      idFieldList.add(field);
      // カラム名追加
      idColumnList.add(idAnnotation.value());
    }
    if (idFieldList.isEmpty()) { throw new InvalidEntityException("Entity:" + clazz.getName() + "にIdアノテーションが指定されていません"); }

    dataBaseInfo.setIdFieldName(idFieldList.toArray(new Field[idFieldList.size()]));
    dataBaseInfo.setIdColumn(idColumnList.toArray(new String[idColumnList.size()]));

    // キャッシュに追加
    dbInfoMap.put(clazz, dataBaseInfo);

    return dataBaseInfo;
  }

  /**
   * DBのデータをキャッシュしておくオブジェクト
   */
  @Getter
  @Setter
  public static class DataBaseInfo {
    Field[] idFieldName;
    String[] idColumn;
    String tableName;
  }
}

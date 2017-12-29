package jp.thelow.thelowSql.database.test;

import jp.thelow.thelowSql.annotation.Column;
import jp.thelow.thelowSql.annotation.Id;
import jp.thelow.thelowSql.annotation.Table;
import lombok.Data;

/**
 * プレイヤー情報を保存するためのEntity。
 */
@Data
@Table("PLAYER_DATA")
public class TestEntity {

  @Id("UUID")
  @Column("UUID")
  private String uuid;

  @Id("DATA_TYPE")
  @Column("DATA_TYPE")
  private String dataType = "dataA";

  /** 剣レベル */
  @Column("SWORD_LEVEL")
  private int swordLevel;

  /** 弓レベル */
  @Column("BOW_LEVEL")
  private int bowLevel;

  /** 魔法レベル */
  @Column("MAGIC_LEVEL")
  private int magicLevel;

  /** 剣経験値 */
  @Column("SWORD_EXP")
  private int swordExp;

  /** 弓経験値 */
  @Column("BOW_EXP")
  private int bowExp;

  /** 魔法経験値 */
  @Column("MAGIC_EXP")
  private int magicExp;

  /** 所持金 */
  @Column("MONEY")
  private int money;

  /** 剣の転生IDリスト */
  @Column("SWORD_REINC_KEYLIST")
  private String swordReincKeyList;

  /** 弓の転生IDリスト */
  @Column("BOW_REINC_KEYLIST")
  private String bowReincKeyList;

  /** 魔法の転生IDリスト */
  @Column("MAGIC_REINC_KEYLIST")
  private String magicReincKeyList;

}

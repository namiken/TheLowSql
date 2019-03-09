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
  private String dataType;

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

  /** banID */
  @Column("BAN_ID")
  private String banId;

  /** ユニット */
  @Column("UNIT")
  private int unit;

  /** 剣の転生回数 */
  @Column("SWORD_REINC_COUNT")
  private int swordReincCount;

  /** 弓の転生回数 */
  @Column("BOW_REINC_COUNT")
  private int bowReincCount;

  /** 魔法の転生回数 */
  @Column("MAGIC_REINC_COUNT")
  private int magicReincCount;

  /** 取得パークリスト */
  @Column("PARK_KEYLIST")
  private String parkKeylist;

}

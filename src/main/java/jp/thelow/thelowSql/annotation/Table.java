package jp.thelow.thelowSql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
  /**
   * テーブル名を取得
   *
   * @return
   */
  String value();

  String threadName() default "original";
}

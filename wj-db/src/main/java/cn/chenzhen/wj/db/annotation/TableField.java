package cn.chenzhen.wj.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface TableField {
    /**
     * 数据库字段名称
     * @return 数据库字段名称
     */
    String value() default "";
    /**
     * 是否为主键
     * 删改查时生成SQL的条件
     * @return 默认false
     */
    boolean primaryKey() default false;

    /**
     * 序列化和反序列化时是否忽略该字段
     * @return 默认不忽略
     */
    boolean ignore() default false;
}

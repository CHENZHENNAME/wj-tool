package cn.chenzhen.wj.db.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 数据库字段名称
     * @return 数据库字段名称
     */
    String value() default "";
}

package cn.chenzhen.wj.json.bean.annotation;

import java.lang.annotation.*;

/**
 * json 序列化和反序列化时使用的配置
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Json {
    /**
     * json属性名称
     * @return 属性名称
     */
    String value() default "";

    /**
     * 序列化和反序列化时是否忽略该字段
     * @return 默认不忽略
     */
    boolean ignore() default false;

    /**
     * 日期或者时间对象 序列号和反序列化时使用的格式
     * @return 格式
     */
    String pattern() default "";
}

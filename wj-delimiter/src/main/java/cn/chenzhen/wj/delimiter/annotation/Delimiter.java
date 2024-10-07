package cn.chenzhen.wj.delimiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Delimiter {

    /**
     * 字符集编码 默认utf-8
     * @return 字符集编码
     */
    // String charset() default "UTF-8";

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

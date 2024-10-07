package cn.chenzhen.wj.xml.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface Xml {
    /**
     * xml 节点名称
     * @return 节点名称
     */
    String value() default "";

    /**
     * Xml属性
     * @return Xml属性
     */
    Attribute[] attribute() default {};
    /**
     * 日期或者时间对象 序列号和反序列化时使用的格式
     * @return 格式
     */
    String pattern() default "";

    /**
     * 序列化时是否忽略属性
     * @return 默认不忽略
     */
    boolean ignore() default false;
}

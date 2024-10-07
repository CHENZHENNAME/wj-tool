package cn.chenzhen.wj.xml.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Attr {
    /**
     * 属性节点名称
     * @return 属性节点名称
     */
    String value() default "";
}

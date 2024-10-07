package cn.chenzhen.wj.xml.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Attribute {
    /**
     * 属性名称
     * @return 属性名称
     */
    String attrName();

    /**
     * 属性值
     * @return 属性值
     */
    String attrValue();
}

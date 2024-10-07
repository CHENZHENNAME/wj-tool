package cn.chenzhen.wj.fix.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定长字段 配置注解
 * 由于方法顺序是随机的 所以以定义的字段作为顺序
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Fix {
    /**
     * 定长字段长度 字节
     * 如果长度为 小于等于0 不进行校验
     * 反序列化时 如果为0 所有剩下的数据全部赋值给这个字段 一般用于最后一个不确定长度字段
     * @return 长度
     */
    int value() default 0;

    /**
     * 填充内容 默认填充空格
     * @return 填充内容
     */
    byte padding() default ' ';

    /**
     * 填充方式 默认右边填充
     * @return 填充方式
     */
    PaddingType paddingType() default PaddingType.RIGHT;

    /**
     * 字符集编码 默认utf-8
     * @return 字符集编码
     */
    String charset() default "UTF-8";
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

    /**
     * 序列化时设置字段的值为 指定字段数组或者集合的大小
     * 反序列化时如果当前类型是集合或者数组 取指定字段的值进行遍历生成集合
     * @return 字段名称
     */
    String valueIsFieldSize() default "";

    /**
     * 反序列化时 循环的次数 只对数组和集合有用
     * @return 循环次数
     */
    int size() default 0;
}

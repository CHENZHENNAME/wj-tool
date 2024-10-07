package cn.chenzhen.wj.delimiter;

import cn.chenzhen.wj.delimiter.processor.TextProcessor;
import cn.chenzhen.wj.reflect.ClassUtil;
import cn.chenzhen.wj.delimiter.annotation.Delimiter;
import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class BeanToText {
    private DelimiterConfig config;
    private String delimiter;
    private TextProcessor textProcessor;

    public BeanToText(DelimiterConfig config) {
        this.config = config;
        this.delimiter = config.getDelimiter();
        textProcessor = config.getTextProcessor();
    }
    public String toText(Object bean) {
        return objectToText(bean);
    }

    /**
     * 把对象转换为 分隔符字符串
     * @param bean 对象
     * @return 分隔符字符串
     */
    private String objectToText(Object bean) {
        if (bean == null) {
            return null;
        }
        Class<?> cls = bean.getClass();
        List<Field> list = ClassUtil.getFieldList(cls);
        StringJoiner joiner = new StringJoiner(delimiter);
        for (Field field : list) {
            Delimiter ann = field.getAnnotation(Delimiter.class);
            if (ann != null && ann.ignore()) {
                continue;
            }
            Object value = ClassUtil.getFieldValue(bean, field);
            String text = valueConvertToText(ann, value);
            joiner.add(text);
        }
        return joiner.toString();
    }

    /**
     * 把字段值转换为分隔符字符串
     * @param ann 注解
     * @param value 字段值
     * @return 分隔符字符串
     */
    public String valueConvertToText(Delimiter ann, Object value) {
        if (value == null) {
            return "";
        }
        // 基本类型
        Object simpleValue = TypeUtil.serialize(value);
        if (simpleValue != null) {
            String text = textProcessor.serialize(config, String.valueOf(simpleValue));
            return text;
        }
        // 日期类型
        String pattern = config.getPattern(value);
        if (ann != null) {
            pattern = ann.pattern();
        }
        String date = DateUtil.format(value, pattern);
        if (date != null) {
            String text = textProcessor.serialize(config, date);
            return text;
        }
        // 数组
        if (value.getClass().isArray()) {
            return arrayToText(ann, value);
        }
        // 集合
        if (value instanceof Collection) {
            return collectionToText(ann, (Collection<?>)value);
        }
        // 自定义对象
        return objectToText(value);
    }

    /**
     * 集合转换为  分隔符字符串
     * @param ann 注解
     * @param collection 集合
     * @return 分隔符字符串
     */
    private String collectionToText(Delimiter ann, Collection<?> collection) {
        StringJoiner joiner = new StringJoiner(delimiter);
        for (Object item : collection) {
            String val = valueConvertToText(ann, item);
            joiner.add(val);
        }
        return joiner.toString();
    }

    /**
     * 数组转换为分隔符字符串
     * @param ann 注解
     * @param value 数组
     * @return 分隔符字符串
     */
    private String arrayToText(Delimiter ann, Object value) {
        int length = Array.getLength(value);
        StringJoiner joiner = new StringJoiner(delimiter);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(value, i);
            String val = valueConvertToText(ann, item);
            joiner.add(val);
        }
        return joiner.toString();
    }
}

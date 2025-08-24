package cn.chenzhen.wj.delimiter;

import cn.chenzhen.wj.delimiter.annotation.Delimiter;
import cn.chenzhen.wj.reflect.ClassUtil;
import cn.chenzhen.wj.reflect.TypeReference;
import cn.chenzhen.wj.type.BeanClassTypeFactory;
import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeUtil;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.List;

public class TextToBean {
    private DelimiterConfig config;
    private  int index = 0;
    private List<String> fieldValueList;
    private boolean indexFlag = false;


    public TextToBean(DelimiterConfig config) {
        this.config = config;
    }

    /**
     * 把分隔符字段转换为目标对象
     * @param reference 类型
     * @return 对象
     * @param <T> 类型
     */
    public <T> T toBean(String text, TypeReference<T> reference) {
        return toBean(text, reference.getType());
    }
    /**
     * 把分隔符字段转换为目标对象
     * @param type 类型
     * @return 对象
     * @param <T> 类型
     */
    public  <T> T toBean(String text, Type type) {
        fieldValueList = config.getTextProcessor().deserializer(config, text);
        if (fieldValueList == null) {
            return null;
        }
        return toBean(type);
    }

    /**
     * 把字段转换为目标对象
     * @param type 类型
     * @return 对象
     * @param <T> 类型
     */
    @SuppressWarnings("unchecked")
    private <T> T toBean(Type type) {
        Class<?> targetType = ClassUtil.getTypeClass(type);
        Object bean = BeanClassTypeFactory.createBean(targetType);
        List<Field> list = ClassUtil.getFieldList(targetType);
        for (Field field : list) {
            Delimiter ann = field.getAnnotation(Delimiter.class);
            if (ann != null && ann.ignore()) {
                continue;
            }
            Type sourceType = field.getGenericType();
            if (sourceType instanceof TypeVariable) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                sourceType = parameterizedType.getActualTypeArguments()[0];
            }

            Object result = null;
            if (sourceType instanceof GenericArrayType) {
                // 泛型数组
                result = toArray(ann, sourceType);
            } else {
                result = convertValue(ann, sourceType);
            }
            if (result == null) {
                continue;
            }
            ClassUtil.setFieldValue(bean, field, result);
        }
        return (T)bean;

    }

    /**
     * 读取一个字段转换为目标类型
     * @param ann 注解信息
     * @param type 目标类型
     * @return 转后对象
     */
    private Object convertValue(Delimiter ann, Type type) {
        String val = getValue();
        if (val == null || val.trim().isEmpty()) {
            return null;
        }
        Class<?> targetType = ClassUtil.getTypeClass(type);
        // 基本类型
        Object result = TypeUtil.deserializer(val, targetType);
        if (result != null) {
            return result;
        }
        // 日期类型
        String pattern = config.getPattern(targetType);
        if (ann != null) {
            pattern = ann.pattern();
        }
        Object date = DateUtil.parse(val, pattern, targetType);
        if (date != null) {
            return date;
        }
        // 数组类型
        if (targetType.isArray()) {
            return toArray(ann, type);
        }
        // 集合类型
        if (Collection.class.isAssignableFrom(targetType)) {
            return toCollection(ann, type);
        }
        // 自定义对象类型
        return toBean(type);
    }

    /**
     * 读取 剩下的字段转换为一个数组
     * @param ann 注解信息
     * @param type 数组类型
     * @return 数组对象
     */
    private Object toArray(Delimiter ann, Type type) {
        Class<?> targetType = ClassUtil.getTypeClass(type);
        if (targetType.isArray()) {
            targetType = targetType.getComponentType();
        }
        List<Object> list = toCollection(ann, type);
        Object o = Array.newInstance(targetType, list.size());
        list.toArray((Object[])o);
        return o;
    }

    /**
     * 读取 剩下的字段转换为一个集合
     * @param ann 注解信息
     * @param type 类型
     * @return 集合对象
     */
    @SuppressWarnings("unchecked")
    private List<Object> toCollection(Delimiter ann, Type type) {
        Class<?> targetType = ClassUtil.getTypeClass(type);
        List<Object> list = (List<Object>) BeanClassTypeFactory.createBean(targetType);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type argument = parameterizedType.getActualTypeArguments()[0];
        while(!indexFlag) {
            Object result = convertValue(ann, argument);
            list.add(result);
        }
        return list;
    }

    /**
     * 尝试读取一个字段值
     * @return 值
     */
    private String getValue(){
        if (indexFlag) {
            return null;
        }
        if (index >= fieldValueList.size()) {
            indexFlag = true;
            return null;
        }
        return fieldValueList.get(index++);
    }
}

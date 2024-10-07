package cn.chenzhen.wj.delimiter;

import cn.chenzhen.wj.delimiter.annotation.Delimiter;
import cn.chenzhen.wj.reflect.ClassUtil;
import cn.chenzhen.wj.reflect.GenericType;
import cn.chenzhen.wj.reflect.TypeReference;
import cn.chenzhen.wj.type.BeanClassTypeFactory;
import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
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
     * @param clazz 类型
     * @return 对象
     * @param <T> 类型
     */
    public <T> T toBean(String text, Class<T> clazz) {
        return toBean(text, new GenericType(clazz));
    }

    /**
     * 把分隔符字段转换为目标对象
     * @param reference 类型
     * @return 对象
     * @param <T> 类型
     */
    public <T> T toBean(String text, TypeReference<T> reference) {
        return toBean(text, reference.getGenericType());
    }
    /**
     * 把分隔符字段转换为目标对象
     * @param genericType 类型
     * @return 对象
     * @param <T> 类型
     */
    private  <T> T toBean(String text, GenericType genericType) {
        fieldValueList = config.getTextProcessor().deserializer(config, text);
        if (fieldValueList == null) {
            return null;
        }
        return toBean(genericType);
    }

    /**
     * 把字段转换为目标对象
     * @param type 类型
     * @return 对象
     * @param <T> 类型
     */
    @SuppressWarnings("unchecked")
    private <T> T toBean(GenericType type) {
        Class<?> targetType = type.getType();
        Object bean = BeanClassTypeFactory.createBean(targetType);
        List<Field> list = ClassUtil.getFieldList(targetType);
        for (Field field : list) {
            Delimiter ann = field.getAnnotation(Delimiter.class);
            if (ann != null && ann.ignore()) {
                continue;
            }
            Type sourceType = field.getGenericType();
            if (sourceType instanceof Class) {
                type = ClassUtil.typeReference(sourceType).getGenericType();
            } else {
                // 泛型处理
                GenericType beanType = type.getGenericType().get(sourceType.getTypeName());
                if (beanType == null) {
                    type = ClassUtil.typeReference(sourceType).getGenericType();
                }
            }
            Object result = null;
            if (sourceType instanceof GenericArrayType) {
                // 泛型数组
                result = toArray(ann, type);
            } else {
                result = convertValue(ann, type);
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
    private Object convertValue(Delimiter ann, GenericType type) {
        String val = getValue();
        if (val == null || val.trim().isEmpty()) {
            return null;
        }
        Class<?> targetType = type.getType();
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
    private Object toArray(Delimiter ann, GenericType type) {
        Class<?> targetType = type.getType();
        if (targetType.isArray()) {
            targetType = targetType.getComponentType();
            type = new GenericType(targetType);
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
    private List<Object> toCollection(Delimiter ann, GenericType type) {
        Class<?> targetType = type.getType();
        List<Object> list = (List<Object>) BeanClassTypeFactory.createBean(targetType);
        Class<?> cls = type.getType();
        String typeName = cls.getTypeParameters()[0].getTypeName();
        GenericType genericType = type.getGenericType().get(typeName);
        while(!indexFlag) {
            Object result = convertValue(ann, genericType);
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

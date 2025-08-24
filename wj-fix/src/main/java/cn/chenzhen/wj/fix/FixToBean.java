package cn.chenzhen.wj.fix;

import cn.chenzhen.wj.fix.annotation.Fix;
import cn.chenzhen.wj.reflect.ClassUtil;
import cn.chenzhen.wj.reflect.TypeReference;
import cn.chenzhen.wj.type.BeanClassTypeFactory;
import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

public class FixToBean {
    private FixConfig config;
    private InputStream in;
    private boolean readFlag = true;
    public FixToBean(FixConfig config) {
        this.config = config;
    }
    public <T> T fixToBean(byte[] data, Class<T> clazz) {
        Type type = ClassUtil.typeReference(clazz).getType();
        return fixToBean(data, type);
    }
    public <T> T fixToBean(byte[] data, TypeReference<T> reference) {
        return fixToBean(data, reference.getType());
    }
    public <T> T fixToBean(byte[] data, Type type) {
        if (data == null || data.length == 0) {
            return null;
        }
        in = new ByteArrayInputStream(data);
        return fixToBean(type);
    }
    @SuppressWarnings("unchecked")
    private <T> T fixToBean(Type type) {
        Class<?> clazz = ClassUtil.getTypeClass(type);
        Object bean = BeanClassTypeFactory.createBean(clazz);
        List<Field> list = ClassUtil.getFieldList(clazz);
        for (Field field : list) {
            Fix ann = field.getAnnotation(Fix.class);
            // 没有长度信息 不处理
            Type genericType = field.getGenericType();
            Type filedType = field.getType();
            if (genericType instanceof TypeVariable) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                filedType = ClassUtil.getVariableType(parameterizedType, ((TypeVariable<?>) genericType).getName());
            }
            Object val = null;
            if (filedType instanceof GenericArrayType) {
                // 泛型数组
                int arraySize = arraySize(bean, ann);
                val = fixToArray(arraySize, ann, filedType);
            } else {
                val = readValue(bean, ann, filedType);
            }
            ClassUtil.setFieldValue(bean, field, val);
        }
        return (T)bean;
    }
    private Object readValue(Object bean, Fix ann, Type type) {
        int size = 0;
        String pattern = config.getPattern(type);
        Charset charset = config.getCharset();
        if (ann != null) {
            size = ann.value();
            pattern = ann.pattern();
            charset = Charset.forName(ann.charset());
        }
        Class<?> clazz = ClassUtil.getTypeClass(type);
        // 基本类型
        if (TypeUtil.getConvertService(clazz) != null) {
            String val  = readBytes(size, charset);
            return TypeUtil.deserializer(val, clazz);
        }
        // 日期类型
        if (DateUtil.getConvertService(clazz) != null) {
            String val  = readBytes(size, charset);
            return DateUtil.parse(val, pattern, clazz);
        }
        // 数组
        if (clazz.isArray()) {
            int arraySize = arraySize(bean, ann);

            // 基本对象
            // 自定义对象
            return fixToArray(arraySize, ann, type);
        }
        // 集合
        if (Collection.class.isAssignableFrom(clazz)) {
            int arraySize = arraySize(bean, ann);
            // 基本对象
            // 自定义对象
            return fixToCollection(arraySize, ann, type);
        }
        // map 不考虑
        // 自定义对象
        return fixToBean(type);
    }
    private int arraySize(Object bean, Fix ann) {
        if (ann != null) {
            String fieldSize = ann.valueIsFieldSize();
            if (!fieldSize.isEmpty()) {
                Field field = ClassUtil.getField(bean.getClass(), fieldSize);
                return Integer.parseInt(String.valueOf(ClassUtil.getFieldValue(bean, field)));
            }
            return ann.size();
        }
        return 0;
    }
    @SuppressWarnings("unchecked")
    private Object fixToCollection(int size, Fix ann, Type type){
        Class<?> clazz = ClassUtil.getTypeClass(type);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type argument = parameterizedType.getActualTypeArguments()[0];
        List<Object> list = (List<Object>)BeanClassTypeFactory.createBean(clazz);
        // 如果最后一个数下 不知道 为0 的情况
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                Object val = readValue(null, ann, argument);
                list.add(val);
            }
        } else {
            while (readFlag) {
                Object val = readValue(null, ann, argument);
                list.add(val);
            }
        }
        return list;
    }
    private Object fixToArray(int size, Fix ann, Type type){
        Class<?> clazz = ClassUtil.getTypeClass(type);
        if (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        List<?> list = (List<?>) fixToCollection(size, ann, type);
        size = list.size();
        Object array = Array.newInstance(clazz);
        for (int i = 0; i < size; i++) {
            Array.set(array, i, list.get(i));
        }
        return array;
    }
    private String readBytes(int size, Charset charset) {
        try {
            if (size <= 0) {
                while (true) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    int code = in.read();
                    if (code == -1) {
                        readFlag = false;
                        return new String(os.toByteArray(), charset).trim();
                    }
                    os.write(code);
                }
            }
            byte[] bytes = new byte[size];
            int code = in.read(bytes);
            if (code == -1 || code < size) {
                readFlag = false;
            }
            return new String(bytes, charset).trim();
        } catch (IOException e) {
            throw new FixException(e);
        }
    }

}

package cn.chenzhen.wj.fix;

import cn.chenzhen.wj.fix.annotation.Fix;
import cn.chenzhen.wj.reflect.ClassUtil;
import cn.chenzhen.wj.reflect.GenericType;
import cn.chenzhen.wj.reflect.TypeReference;
import cn.chenzhen.wj.type.BeanClassTypeFactory;
import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
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
        GenericType type = ClassUtil.typeReference(clazz).getGenericType();
        return fixToBean(data, type);
    }
    public <T> T fixToBean(byte[] data, TypeReference<T> reference) {
        return fixToBean(data, reference.getGenericType());
    }
    public <T> T fixToBean(byte[] data, GenericType type) {
        if (data == null || data.length == 0) {
            return null;
        }
        in = new ByteArrayInputStream(data);
        return fixToBean(type);
    }
    @SuppressWarnings("unchecked")
    private <T> T fixToBean(GenericType type) {
        Class<?> clazz = type.getType();
        Object bean = BeanClassTypeFactory.createBean(clazz);
        List<Field> list = ClassUtil.getFieldList(clazz);
        for (Field field : list) {
            Fix ann = field.getAnnotation(Fix.class);
            // 没有长度信息 不处理
            Type genericType = field.getGenericType();
            if (genericType instanceof Class) {
                type = ClassUtil.typeReference(genericType).getGenericType();
            } else {
                // 泛型处理
                GenericType beanType = type.getGenericType().get(genericType.getTypeName());
                if (beanType == null) {
                    type = ClassUtil.typeReference(genericType).getGenericType();
                }
            }
            Object val = null;
            if (genericType instanceof GenericArrayType) {
                // 泛型数组
                int arraySize = arraySize(bean, ann);
                val = fixToArray(arraySize, ann, type);
            } else {
                val = readValue(bean, ann, type);
            }
            ClassUtil.setFieldValue(bean, field, val);
        }
        return (T)bean;
    }
    private Object readValue(Object bean, Fix ann, GenericType type) {
        int size = 0;
        String pattern = config.getPattern(type);
        Charset charset = config.getCharset();
        if (ann != null) {
            size = ann.value();
            pattern = ann.pattern();
            charset = Charset.forName(ann.charset());
        }
        Class<?> clazz = type.getType();
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
    private Object fixToCollection(int size, Fix ann, GenericType type){
        Class<?> cls = type.getType();
        String typeName = cls.getTypeParameters()[0].getTypeName();
        GenericType genericType = type.getGenericType().get(typeName);
        List<Object> list = (List<Object>)BeanClassTypeFactory.createBean(cls);
        // 如果最后一个数下 不知道 为0 的情况
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                Object val = readValue(null, ann, genericType);
                list.add(val);
            }
        } else {
            while (readFlag) {
                Object val = readValue(null, ann, genericType);
                list.add(val);
            }
        }
        return list;
    }
    private Object fixToArray(int size, Fix ann, GenericType type){
        Class<?> clazz = type.getType();
        if (clazz.isArray()) {
            clazz = clazz.getComponentType();
            type = new GenericType(clazz);
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

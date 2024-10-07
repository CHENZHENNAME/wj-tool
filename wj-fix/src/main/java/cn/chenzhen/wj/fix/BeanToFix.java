package cn.chenzhen.wj.fix;

import cn.chenzhen.wj.fix.annotation.Fix;
import cn.chenzhen.wj.fix.annotation.PaddingType;
import cn.chenzhen.wj.reflect.ClassUtil;
import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeUtil;
import cn.chenzhen.wj.util.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

public class BeanToFix {
    private FixConfig config;
    public BeanToFix(FixConfig config) {
        this.config = config;
    }
    public byte[] toFix(Object bean){
        return objectToFix(bean);
    }

    /**
     * 对象转换为 bytes
     * @param bean 对象
     * @return 转后结果
     */
    private byte[] objectToFix(Object bean){
        if (bean == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        List<Field> list = ClassUtil.getFieldList(bean.getClass());
        for (Field field : list) {
            try {
                // 注解目前就一个 也感觉没啥扩展的 暂时写死
                Fix ann = field.getAnnotation(Fix.class);
                Object fieldValue = ClassUtil.getFieldValue(bean, field);
                byte[] bytes = parseValue(bean, ann, fieldValue, field.getName());
                os.write(bytes);
            } catch (IOException ignored) {}
        }
        return os.toByteArray();
    }
    private byte[] parseValue(Object bean, Fix ann, Object value, String name){
        if (value == null && ann == null) {
            return null;
        }
        int length = 0;
        byte padding = ' ';
        String pattern = config.getPattern(value);
        Charset charset = config.getCharset();
        PaddingType type = PaddingType.RIGHT;
        if (ann != null) {
            if (ann.ignore()) {
                return null;
            }
            length = ann.value();
            padding = ann.padding();
            type = ann.paddingType();
            pattern = ann.pattern();
            charset = Charset.forName(ann.charset());
            if (!ann.valueIsFieldSize().isEmpty()) {
                Field field = ClassUtil.getField(bean.getClass(), ann.valueIsFieldSize());
                if (field == null) {
                    throw new FixException("field not found " + ann.valueIsFieldSize());
                }
                value = ClassUtil.getFieldValue(bean, field);
            }
        }
        if (value == null) {
            return padding(null, length, padding, type, name);
        }
        // 基本类型
        Object val = TypeUtil.serialize(value);
        if (val == null) {
            // 日期类型
            val = DateUtil.format(value, pattern);
        }

        if (val != null) {
            // 基本类型 或者日期类型
            return padding(String.valueOf(val).getBytes(charset), length, padding, type, name);
        }
        // 集合类型
        Class<?> cls = value.getClass();
        if (cls.isArray()) {
            // 数组类型
            return arrayToFix(bean, value, ann, name);
        } else if (Collection.class.isAssignableFrom(cls)) {
            // 集合
            return collectionToFix(bean, (Collection<?>) value, ann, name);
        }
        // 不考虑 map类型

        // 自定义类型
        return objectToFix(value);

    }

    /**
     * 集合转换为定长字节
     * @param bean 对象
     * @param value 集合
     * @param ann 注解
     * @return 转换结果
     */
    private byte[] collectionToFix(Object bean, Collection<?> value, Fix ann, String name){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (Object item : value) {
            try {
                byte[] bytes = parseValue(bean, ann, item, name);
                os.write(bytes);
            } catch (IOException ignored) {}
        }
        return os.toByteArray();
    }
    /**
     * 数组转换为定长字节
     * @param bean 对象
     * @param value 值
     * @param ann 注解
     * @return 转换结果
     */
    private byte[] arrayToFix(Object bean, Object value, Fix ann, String name){
        if (value.getClass().getComponentType() == Byte.class) {
            return (byte[])value;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            try {
                Object item = Array.get(value, i);
                byte[] bytes = parseValue(bean, ann, item, name);
                os.write(bytes);
            } catch (IOException ignored) {}
        }
        return os.toByteArray();
    }

    private static byte[] padding(byte[] bytes, int len, byte code, PaddingType type, String name) {
        if (len == 0) {
            return bytes;
        }
        if (type == PaddingType.LEFT) {
            bytes = StringUtil.padLeft(bytes, len, code);
        }else if (type == PaddingType.RIGHT) {
            bytes = StringUtil.padRight(bytes, len,  code);
        }
        if (bytes.length > len) {
            // 长度检查
            throw new FixException(name + " length exceeds max length of " + len);
        }
        return bytes;
    }
}

package cn.chenzhen.wj.type;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeUtil {
    private static List<Class<?>> PRIMITIVE_LIST = new ArrayList<Class<?>>();
    static {
        PRIMITIVE_LIST.add(byte.class);
        PRIMITIVE_LIST.add(Byte.class);
        PRIMITIVE_LIST.add(short.class);
        PRIMITIVE_LIST.add(Short.class);
        PRIMITIVE_LIST.add(int.class);
        PRIMITIVE_LIST.add(Integer.class);
        PRIMITIVE_LIST.add(long.class);
        PRIMITIVE_LIST.add(Long.class);
        PRIMITIVE_LIST.add(float.class);
        PRIMITIVE_LIST.add(Float.class);
        PRIMITIVE_LIST.add(double.class);
        PRIMITIVE_LIST.add(Double.class);
        PRIMITIVE_LIST.add(boolean.class);
        PRIMITIVE_LIST.add(Boolean.class);
        PRIMITIVE_LIST.add(char.class);
        PRIMITIVE_LIST.add(Character.class);
        PRIMITIVE_LIST.add(String.class);
        PRIMITIVE_LIST.add(BigDecimal.class);
        PRIMITIVE_LIST.add(BigInteger.class);
        PRIMITIVE_LIST.add(OffsetTime.class);
        PRIMITIVE_LIST.add(LocalDate.class);
        PRIMITIVE_LIST.add(LocalDateTime.class);
        PRIMITIVE_LIST.add(ZonedDateTime.class);
        PRIMITIVE_LIST.add(Instant.class);

    }

    /**
     * 是否为基本类型
     * @param clazz 类型
     * @return 是否为基本类型
     */
    public static  boolean isPrimitive(Class<?> clazz){
        return PRIMITIVE_LIST.contains(clazz);
    }

    /**
     * 判断对象是否为数组或者集合
     * @param data 对象
     * @return true 数组或者集合
     */
    public static boolean isArrayOrCollection(Object data){
        if (data == null) {
            return false;
        }
        Class<?> type = data.getClass();
        if (type.isArray()) {
            return true;
        }
        if (Collection.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }
}

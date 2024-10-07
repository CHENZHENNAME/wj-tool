package cn.chenzhen.wj.type.convert;


import cn.chenzhen.wj.type.convert.service.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeUtil {
    private static final Map<Class<?>, ConvertService<?>> TYPE_MAP = new HashMap<>();

    static {
        initDefaultType();
    }
    private static void initDefaultType(){
        ByteService byteService = new ByteService();
        register(byte.class, byteService);
        register(Byte.class, byteService);
        ShortService shortService = new ShortService();
        register(short.class, shortService);
        register(Short.class, shortService);
        IntService intService = new IntService();
        register(int.class, intService);
        register(Integer.class, intService);
        LongService longService = new LongService();
        register(long.class, longService);
        register(Long.class, longService);
        FloatService floatService = new FloatService();
        register(float.class, floatService);
        register(Float.class, floatService);
        DoubleService doubleService = new DoubleService();
        register(double.class, doubleService);
        register(Double.class, doubleService);
        BooleanService booleanService = new BooleanService();
        register(boolean.class, booleanService);
        register(Boolean.class, booleanService);
        CharService charService = new CharService();
        register(char.class, charService);
        register(Character.class, charService);
        register(String.class, new StringService());
        register(BigDecimal.class, new BigDecimalService());
        register(BigInteger.class, new BigIntegerService());

    }

    /**
     * 注册类型转换器
     * @param clazz 目标类型
     * @param convertService 转换器
     */
    public static void register(Class<?> clazz, ConvertService<?> convertService) {
        TYPE_MAP.put(clazz, convertService);
    }

    /**
     * 删除指定类型注册转换器
     * @param clazz 类型
     */
    public static void unregister(Class<?> clazz) {
        TYPE_MAP.remove(clazz);
    }

    /**
     * 获取类型转换器
     * @param clazz 类型
     * @return 转换器
     */
    public static ConvertService<?> getConvertService(Class<?> clazz) {
        return TYPE_MAP.get(clazz);
    }

    /**
     * 数据转换 如果没找到转换器 返回 null
     * @param value 值
     * @param clazz 目标类型
     * @return 转换后的对象 如果没有找到类型转换器 返回null
     */
    public static Object deserializer(Object value, Class<?> clazz) {
        ConvertService<?> service = TYPE_MAP.get(clazz);
        if (service == null) {
            return null;
        }
        return service.deserializer(value);
    }

    /**
     * 序列化类型 如果没找到转换器 返回 null
     * @param value 对象
     * @return 序列号对象
     */
    @SuppressWarnings("unchecked")
    public static Object serialize(Object value){
        if (value == null){
            return null;
        }
        Class<?> cls = value.getClass();
        ConvertService<Object> service = (ConvertService<Object>)TYPE_MAP.get(cls);
        if (service == null) {
            return null;
        }
        return service.serialize(value);
    }
}

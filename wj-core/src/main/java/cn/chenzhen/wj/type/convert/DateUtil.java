package cn.chenzhen.wj.type.convert;


import cn.chenzhen.wj.type.convert.service.date.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期转换器
 */
public class DateUtil {
    public static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static String DEFAULT_TIME_PATTERN = "HH:mm:ss";
    public static String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final Map<Class<?>, ConvertService<?>> DEFAULT_CONVERT_SERVICE = new HashMap<>();
    static {
        register(new CalendarConvertService());
        register(new DateConvertService());
        register(new LocalDateConvertService());
        register(new LocalDateTimeConvertService());
        register(new LocalTimeConvertService());
        register(new ZonedDateTimeConvertService());
        register(new OffsetTimeConvertService());
        register(new OffsetDateTimeConvertService());
        register(new SqlDateConvertService());
        register(new SqlTimeConvertService());
        register(new SqlTimestampConvertService());
    }

    /**
     * 注册日期转换器
     * @param convertService 日期转换器
     */
    public static void register(ConvertService<?> convertService) {
        DEFAULT_CONVERT_SERVICE.put(convertService.type(), convertService);
    }

    /**
     * 删除日期转换器
     * @param type 类型
     */
    public static void unregister(Class<?> type) {
        DEFAULT_CONVERT_SERVICE.remove(type);
    }

    /**
     * 获取日期转换器
     * @param type 日期类型
     * @return 转换器
     */
    public static ConvertService<?> getConvertService(Class<?> type) {
        ConvertService<?> service = DEFAULT_CONVERT_SERVICE.get(type);
        if (service != null) {
            return service;
        }
        // 继承的特殊处理
        if (Calendar.class.isAssignableFrom(type)) {
            return DEFAULT_CONVERT_SERVICE.get(Calendar.class);
        }
        return null;
    }

    /**
     * 日期转换为字符串
     * @param date 日期对象
     * @param pattern 转换格式如果为空使用默认格式
     * @return 日期字符串
     */
    @SuppressWarnings("unchecked")
    public static String format(Object date, String pattern) {
        if (date == null) {
            return null;
        }
        Class<?> type = date.getClass();
        ConvertService<Object> service = (ConvertService<Object>)getConvertService(type);
        if (service == null) {
            return null;
        }
        return service.serialize(date, pattern);
    }

    /**
     * 将字符串或者时间戳转换为日期对象
     * @param date 字符串或时间戳
     * @param pattern 日期格式
     * @return 日期对象
     */
    @SuppressWarnings("unchecked")
    public static Object parse(Object date, String pattern, Class<?> type) {
        if (date == null) {
            return null;
        }
        ConvertService<Object> service = (ConvertService<Object>)getConvertService(type);
        if (service == null) {
            return null;
        }
        return service.deserializer(date, pattern);
    }
}

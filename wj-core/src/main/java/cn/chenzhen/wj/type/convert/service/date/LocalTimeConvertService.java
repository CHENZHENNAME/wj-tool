package cn.chenzhen.wj.type.convert.service.date;



import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeException;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalTimeConvertService implements ConvertService<LocalTime> {
    @Override
    public Class<LocalTime> type() {
        return LocalTime.class;
    }

    @Override
    public LocalTime deserializer(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalTime) {
            return (LocalTime) value;
        }
        if (value instanceof String) {
            if (format == null || format.isEmpty()) {
                format = DateUtil.DEFAULT_TIME_PATTERN;
            }

            return LocalTime.parse((String)value, DateTimeFormatter.ofPattern(format));
        }
        if (value instanceof Number) {
            return Instant.ofEpochMilli(((Number) value).longValue()).atZone(ZoneId.systemDefault()).toLocalTime();
        }
        throw new TypeException("can not cast to LocalTime, value : " + value);
    }

    @Override
    public String serialize(LocalTime value, String format) {
        if (value ==null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = DateUtil.DEFAULT_TIME_PATTERN;
        }
        return value.format(DateTimeFormatter.ofPattern(format));
    }
}

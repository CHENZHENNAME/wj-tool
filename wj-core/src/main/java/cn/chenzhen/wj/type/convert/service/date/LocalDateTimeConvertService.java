package cn.chenzhen.wj.type.convert.service.date;


import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConvertService implements ConvertService<LocalDateTime> {
    @Override
    public Class<LocalDateTime> type() {
        return LocalDateTime.class;
    }

    @Override
    public LocalDateTime deserializer(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }
        if (value instanceof String) {
            if (format == null || format.isEmpty()) {
                format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
            }
            return LocalDateTime.parse((String)value, DateTimeFormatter.ofPattern(format));
        }
        if (value instanceof Number) {
            return Instant.ofEpochMilli(((Number) value).longValue()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        throw new TypeException("can not cast to LocalDateTime, value : " + value);
    }

    @Override
    public String serialize(LocalDateTime value, String format) {
        if (value ==null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
        }
        return value.format(DateTimeFormatter.ofPattern(format));
    }
}

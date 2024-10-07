package cn.chenzhen.wj.type.convert.service.date;

import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateConvertService implements ConvertService<LocalDate> {
    @Override
    public Class<LocalDate> type() {
        return LocalDate.class;
    }

    @Override
    public LocalDate deserializer(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        if (value instanceof String) {
            if (format == null || format.isEmpty()) {
                format = DateUtil.DEFAULT_DATE_PATTERN;
            }
            return LocalDate.parse((String)value, DateTimeFormatter.ofPattern(format));
        }
        if (value instanceof Number) {
            return Instant.ofEpochMilli(((Number) value).longValue()).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        throw new TypeException("can not cast to LocalDateTime, value : " + value);
    }

    @Override
    public String serialize(LocalDate value, String format) {
        if (value ==null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = DateUtil.DEFAULT_DATE_PATTERN;
        }
        return value.format(DateTimeFormatter.ofPattern(format));
    }
}

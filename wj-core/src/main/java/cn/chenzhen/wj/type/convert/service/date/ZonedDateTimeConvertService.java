package cn.chenzhen.wj.type.convert.service.date;


import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class ZonedDateTimeConvertService implements ConvertService<ZonedDateTime> {
    @Override
    public Class<ZonedDateTime> type() {
        return ZonedDateTime.class;
    }

    @Override
    public ZonedDateTime deserializer(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof ZonedDateTime) {
            return (ZonedDateTime) value;
        }
        if (value instanceof String) {
            if (format == null || format.isEmpty()) {
                format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
            }
            LocalDateTime dateTime = LocalDateTime.parse((String) value, DateTimeFormatter.ofPattern(format));
            return dateTime.atZone(Clock.systemDefaultZone().getZone());
        }
        if (value instanceof Number) {
            return Instant.ofEpochMilli(((Number) value).longValue()).atZone(ZoneId.systemDefault());
        }
        throw new TypeException("can not cast to ZonedDateTime, value : " + value);
    }

    @Override
    public String serialize(ZonedDateTime value, String format) {
        if (value ==null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
        }
        return value.format(DateTimeFormatter.ofPattern(format));
    }
}

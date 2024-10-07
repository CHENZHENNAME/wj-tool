package cn.chenzhen.wj.type.convert.service.date;


import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class OffsetDateTimeConvertService implements ConvertService<OffsetDateTime> {
    @Override
    public Class<OffsetDateTime> type() {
        return OffsetDateTime.class;
    }

    @Override
    public OffsetDateTime deserializer(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof OffsetDateTime) {
            return (OffsetDateTime) value;
        }
        if (value instanceof String) {
            if (format == null || format.isEmpty()) {
                format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
            }
            Clock clock = Clock.systemDefaultZone();
            ZoneOffset offset = clock.getZone().getRules().getOffset(clock.instant());
            return LocalDateTime.parse((String) value, DateTimeFormatter.ofPattern(format))
                            .atOffset(offset);
        }
        if (value instanceof Number) {
            return Instant.ofEpochMilli(((Number) value).longValue()).atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }
        throw new TypeException("can not cast to OffsetDateTime, value : " + value);
    }

    @Override
    public String serialize(OffsetDateTime value, String format) {
        if (value ==null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
        }
        return value.format(DateTimeFormatter.ofPattern(format));
    }
}

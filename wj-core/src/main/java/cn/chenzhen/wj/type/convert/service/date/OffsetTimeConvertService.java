package cn.chenzhen.wj.type.convert.service.date;



import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class OffsetTimeConvertService implements ConvertService<OffsetTime> {
    @Override
    public Class<OffsetTime> type() {
        return OffsetTime.class;
    }

    @Override
    public OffsetTime deserializer(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof OffsetTime) {
            return (OffsetTime) value;
        }
        if (value instanceof String) {
            if (format == null || format.isEmpty()) {
                format = DateUtil.DEFAULT_TIME_PATTERN;
            }
            Clock clock = Clock.systemDefaultZone();
            ZoneOffset offset = clock.getZone().getRules().getOffset(clock.instant());
            return LocalTime.parse((String)value, DateTimeFormatter.ofPattern(format)).atOffset(offset);
        }
        if (value instanceof Number) {
            return Instant.ofEpochMilli(((Number) value).longValue()).atZone(ZoneId.systemDefault()).toOffsetDateTime().toOffsetTime();
        }
        throw new TypeException("can not cast to OffsetTime, value : " + value);
    }

    @Override
    public String serialize(OffsetTime value, String format) {
        if (value ==null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = DateUtil.DEFAULT_TIME_PATTERN;
        }
        return value.format(DateTimeFormatter.ofPattern(format));
    }
}

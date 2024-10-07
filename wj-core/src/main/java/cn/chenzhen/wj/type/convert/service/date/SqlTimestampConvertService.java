package cn.chenzhen.wj.type.convert.service.date;

import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SqlTimestampConvertService implements ConvertService<Timestamp> {
    @Override
    public Class<Timestamp> type() {
        return Timestamp.class;
    }

    @Override
    public Timestamp deserializer(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        }
        if (value instanceof String) {
            try {
                if (format == null || format.isEmpty()) {
                    format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
                }
                java.util.Date date = new SimpleDateFormat(format).parse((String) value);
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                throw new TypeException(e);
            }
        }
        if (value instanceof Number) {
            return new Timestamp(((Number) value).longValue());
        }
        throw new TypeException("can not cast to Date, value : " + value);
    }

    @Override
    public String serialize(Timestamp value, String format) {
        if (value ==null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
        }
        return new SimpleDateFormat(format).format(value);
    }
}

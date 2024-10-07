package cn.chenzhen.wj.type.convert.service.date;

import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeException;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SqlTimeConvertService implements ConvertService<Time> {
    @Override
    public Class<Time> type() {
        return Time.class;
    }

    @Override
    public Time deserializer(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof Time) {
            return (Time) value;
        }
        if (value instanceof String) {
            try {
                if (format == null || format.isEmpty()) {
                    format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
                }
                java.util.Date date = new SimpleDateFormat(format).parse((String) value);
                return new Time(date.getTime());
            } catch (ParseException e) {
                throw new TypeException(e);
            }
        }
        if (value instanceof Number) {
            return new Time(((Number) value).longValue());
        }
        throw new TypeException("can not cast to Date, value : " + value);
    }

    @Override
    public String serialize(Time value, String format) {
        if (value ==null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
        }
        return new SimpleDateFormat(format).format(value);
    }
}

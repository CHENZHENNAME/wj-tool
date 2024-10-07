package cn.chenzhen.wj.type.convert.service.date;

import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeException;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SqlDateConvertService implements ConvertService<Date> {
    @Override
    public Class<Date> type() {
        return Date.class;
    }

    @Override
    public Date deserializer(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof String) {
            try {
                if (format == null || format.isEmpty()) {
                    format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
                }
                java.util.Date date = new SimpleDateFormat(format).parse((String) value);
                return new Date(date.getTime());
            } catch (ParseException e) {
                throw new TypeException(e);
            }
        }
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }
        throw new TypeException("can not cast to Date, value : " + value);
    }

    @Override
    public String serialize(Date value, String format) {
        if (value ==null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
        }
        return new SimpleDateFormat(format).format(value);
    }
}

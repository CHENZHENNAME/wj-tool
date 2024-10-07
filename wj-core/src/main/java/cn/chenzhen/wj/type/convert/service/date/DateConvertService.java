package cn.chenzhen.wj.type.convert.service.date;

import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvertService implements ConvertService<Date> {
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
                return new SimpleDateFormat(format).parse((String) value);
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

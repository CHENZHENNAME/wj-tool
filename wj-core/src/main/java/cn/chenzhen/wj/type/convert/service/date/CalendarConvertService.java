package cn.chenzhen.wj.type.convert.service.date;

import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarConvertService implements ConvertService<Calendar> {
    @Override
    public Class<Calendar> type() {
        return Calendar.class;
    }

    @Override
    public Calendar deserializer(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof Calendar) {
            return (Calendar) value;
        }
        if (value instanceof String) {
            try {
                if (format == null || format.isEmpty()) {
                    format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
                }
                Date date = new SimpleDateFormat(format).parse((String) value);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            } catch (ParseException e) {
                throw new TypeException(e);
            }
        }
        if (value instanceof Number) {
            Date date = new Date(((Number) value).longValue());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }
        throw new TypeException("can not cast to Date, value : " + value);
    }

    @Override
    public String serialize(Calendar value, String format) {
        if (value == null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = DateUtil.DEFAULT_DATE_TIME_PATTERN;
        }
        return new SimpleDateFormat(format).format(value.getTime());
    }
}

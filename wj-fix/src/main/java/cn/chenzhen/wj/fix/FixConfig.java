package cn.chenzhen.wj.fix;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;

public class FixConfig {
    private static final FixConfig CONFIG = new FixConfig();
    public static FixConfig global() {
        return CONFIG;
    }
    /**
     * 默认编码
     */
    private Charset charset = StandardCharsets.UTF_8;
    /**
     * 日期格式
     */
    private String datePattern = "yyyy-MM-dd";
    /**
     * 时间格式
     */
    private String timePattern = "HH:mm:ss";
    /**
     * 日期时间格式
     */
    private String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public String getTimePattern() {
        return timePattern;
    }

    public void setTimePattern(String timePattern) {
        this.timePattern = timePattern;
    }

    public String getDateTimePattern() {
        return dateTimePattern;
    }

    public void setDateTimePattern(String dateTimePattern) {
        this.dateTimePattern = dateTimePattern;
    }

    public String getPattern(Class<?> type){
        /*
         默认
        if (
                type == Calendar.class || type == Date.class || type == java.sql.Date.class
                || type == java.sql.Timestamp.class || type == java.sql.Time.class
                || type == LocalDateTime.class || type == ZonedDateTime.class
                || type == OffsetDateTime.class
        ){
            return dateTimePattern;
        }*/

        if (type == LocalDate.class) {
            return datePattern;
        }
        if (type == LocalTime.class || type == OffsetTime.class) {
            return timePattern;
        }
        return dateTimePattern;
    }
    public String getPattern(Object bean){
        if (bean == null){
            return "";
        }
        return getPattern(bean.getClass());
    }
}

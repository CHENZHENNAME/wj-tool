package cn.chenzhen.wj.json;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.*;

public class JsonConfig {
    private static final JsonConfig CONFIG = new JsonConfig();
    public static JsonConfig global() {
        return CONFIG;
    }

    /**
     * 序列化时使用的编码
     */
    private Charset charset = StandardCharsets.UTF_8;
    /**
     * 数字序列化为字符串格式
     */
    private boolean numberToStr = false;
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
    /**
     * 序列化时是否忽略值为 null的属性 默认不忽略
     */
    private boolean ignoreNull = false;

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public boolean isNumberToStr() {
        return numberToStr;
    }

    public void setNumberToStr(boolean numberToStr) {
        this.numberToStr = numberToStr;
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

    public boolean isIgnoreNull() {
        return ignoreNull;
    }

    public void setIgnoreNull(boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
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
}

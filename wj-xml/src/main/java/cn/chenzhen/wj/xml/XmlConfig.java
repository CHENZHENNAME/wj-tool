package cn.chenzhen.wj.xml;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;

public class XmlConfig {
    private static final XmlConfig CONFIG = new XmlConfig();
    public static XmlConfig global() {
        return CONFIG;
    }
    /**
     * 序列化时使用的编码
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

    /**
     * 序列化时 如果标签没有子元素 是否为单标签
     * 如：false: <xml></xml>
     * 如：true: <div/>
     */
    private boolean simpleTag = false;
    /**
     * 标签值是否转换为 Unicode
     */
    private boolean escape = false;

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

    public boolean isSimpleTag() {
        return simpleTag;
    }

    public void setSimpleTag(boolean simpleTag) {
        this.simpleTag = simpleTag;
    }

    public boolean isEscape() {
        return escape;
    }

    public void setEscape(boolean escape) {
        this.escape = escape;
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

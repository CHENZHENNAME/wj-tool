package cn.chenzhen.wj.delimiter;

import cn.chenzhen.wj.delimiter.processor.DefaultTextProcessor;
import cn.chenzhen.wj.delimiter.processor.TextProcessor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;

public class DelimiterConfig {
    private static final DelimiterConfig CONFIG = new DelimiterConfig();
    public static DelimiterConfig global() {
        return CONFIG;
    }
    private TextProcessor textProcessor = new DefaultTextProcessor();
    /**
     * 分割符号
     */
    private String delimiter = "|";
    /**
     * 当字段值出现和分隔符相同的字符时转义为其他字符
     */
    private String escape  = "\\|";

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

    public TextProcessor getTextProcessor() {
        return textProcessor;
    }

    public void setTextProcessor(TextProcessor textProcessor) {
        this.textProcessor = textProcessor;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getEscape() {
        return escape;
    }

    public void setEscape(String escape) {
        this.escape = escape;
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

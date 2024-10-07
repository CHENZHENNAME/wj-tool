package cn.chenzhen.wj.json;

import cn.chenzhen.wj.type.convert.DateUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JsonWrite {
    private static final char COMMA = ',';
    private static final char COLON = ':';
    private static final char MARKS = '"';
    private static final char EMPTY = ' ';
    private static final char LF = '\n';
    private static final char JSON_TAG_CB_START = '{';
    private static final char JSON_TAG_CB_END = '}';
    private static final char JSON_TAG_SB_START = '[';
    private static final char JSON_TAG_SB_END = ']';
    private static final Map<Class<?>, WriteValue> WRITE_VALUE_MAP = new ConcurrentHashMap<>();
    interface WriteValue {
        String serialize(Object data, JsonConfig config);
    }
    private static String convertString(String value){
        return MARKS + value.toString().replace("\"", "\\\"") + MARKS;
    }
    static {
        // 基本类型处理
        WRITE_VALUE_MAP.put(String.class, (data, config) -> convertString((String) data));
        WRITE_VALUE_MAP.put(char.class, (data, config) -> convertString(String.valueOf(data)));
        WRITE_VALUE_MAP.put(Character.class, (data, config) -> convertString(String.valueOf(data)));
        WriteValue writeValue =   (data, config) ->  {
            if (config.isNumberToStr()) {
                return convertString(data.toString());
            }
            return data.toString();
        };
        WRITE_VALUE_MAP.put(int.class, writeValue);
        WRITE_VALUE_MAP.put(Integer.class, writeValue);
        WRITE_VALUE_MAP.put(long.class, writeValue);
        WRITE_VALUE_MAP.put(Long.class, writeValue);
        WRITE_VALUE_MAP.put(float.class, writeValue);
        WRITE_VALUE_MAP.put(Float.class, writeValue);
        WRITE_VALUE_MAP.put(double.class, writeValue);
        WRITE_VALUE_MAP.put(Double.class, writeValue);
        WRITE_VALUE_MAP.put(boolean.class, (data, config) -> data.toString());
        WRITE_VALUE_MAP.put(Boolean.class, (data, config) -> data.toString());
        WRITE_VALUE_MAP.put(BigDecimal.class, (data, config) ->  {
            String val = ((BigDecimal) data).toPlainString();
            if (config.isNumberToStr()) {
                return MARKS + val + MARKS;
            }
            return val;
        });
        WRITE_VALUE_MAP.put(BigInteger.class, writeValue);
        // 日期处理
        WRITE_VALUE_MAP.put(Calendar.class, (data, config) -> convertString(DateUtil.format(data, config.getDateTimePattern())));
        WRITE_VALUE_MAP.put(Date.class, (data, config) -> convertString(DateUtil.format(data, config.getDateTimePattern())));
        WRITE_VALUE_MAP.put(LocalDate.class, (data, config) -> convertString(DateUtil.format(data, config.getDatePattern())));
        WRITE_VALUE_MAP.put(LocalDateTime.class, (data, config) -> convertString(DateUtil.format(data, config.getDateTimePattern())));
        WRITE_VALUE_MAP.put(OffsetDateTime.class, (data, config) -> convertString(DateUtil.format(data, config.getDateTimePattern())));
        WRITE_VALUE_MAP.put(ZonedDateTime.class, (data, config) -> convertString(DateUtil.format(data, config.getDateTimePattern())));
        WRITE_VALUE_MAP.put(LocalTime.class, (data, config) -> convertString(DateUtil.format(data, config.getTimePattern())));
        WRITE_VALUE_MAP.put(OffsetTime.class, (data, config) -> convertString(DateUtil.format(data, config.getTimePattern())));
        WRITE_VALUE_MAP.put(java.sql.Date.class, (data, config) -> convertString(DateUtil.format(data, config.getDateTimePattern())));
        WRITE_VALUE_MAP.put(java.sql.Time.class, (data, config) -> convertString(DateUtil.format(data, config.getDateTimePattern())));
        WRITE_VALUE_MAP.put(java.sql.Timestamp.class, (data, config) -> convertString(DateUtil.format(data, config.getDateTimePattern())));

    }
    private JsonConfig config = JsonConfig.global();
    private ByteArrayOutputStream out;
    private Object data;


    public JsonWrite(Object data) {
        this.data = data;
        out = new ByteArrayOutputStream();
    }

    public JsonWrite(JsonConfig config, Object data) {
        this(data);
        this.config = config;
    }

    public String toJson() throws IOException {
        // 转换为json对象
        Object json = JsonUtil.besnToJsonObject(data);
        if (json == null) {
            return null;
        }
        if (json instanceof String) {
            return (String) json;
        }
        if (json instanceof JsonObject) {
            writeJsonObject((JsonObject) json);
        } else if (json instanceof JsonArray) {
            writeJsonArray((JsonArray) json);
        }
        return out.toString();
    }
    private void writeJsonObject(JsonObject json) throws IOException {
        out.write(JSON_TAG_CB_START);
        Map<String, Object> map = json.getData();
        Set<Map.Entry<String, Object>> itr = map.entrySet();
        boolean flag = false;
        for (Map.Entry<String, Object> entry : itr) {
            String key = entry.getKey();
            Object val = entry.getValue();
            // 序列化时是否忽略值为 null的属性
            if (val == null && config.isIgnoreNull()) {
                continue;
            }
            if (flag) {
                out.write(COMMA);
            }
            writeItem(key, val);
            flag = true;
        }
        out.write(JSON_TAG_CB_END);
    }
    private void writeJsonArray(JsonArray json) throws IOException {
        out.write(JSON_TAG_SB_START);
        List<Object> list = json.getList();
        boolean flag = false;
        for (Object obj : list) {
            if (flag) {
                out.write(COMMA);
            }
            writeItemValue(obj);
            flag = true;
        }
        out.write(JSON_TAG_SB_END);
    }
    private void writeItem(String key, Object val) throws IOException {
        out.write(MARKS);
        out.write(key.getBytes());
        out.write(MARKS);
        out.write(COLON);
        writeItemValue(val);
    }
    private void writeItemValue(Object value) throws IOException {
        if (value == null) {
            out.write("null".getBytes());
            return;
        }
        WriteValue writeValue = WRITE_VALUE_MAP.get(value.getClass());
        if (writeValue != null) {
            String jsonVal = writeValue.serialize(value, config);
            out.write(jsonVal.getBytes());
            return;
        }
        if ( value instanceof JsonObject) {
            writeJsonObject((JsonObject) value);
        } else if ( value instanceof JsonArray) {
            writeJsonArray((JsonArray) value);
        } else if (value instanceof Calendar) {
            String val = WRITE_VALUE_MAP.get(Calendar.class).serialize(value, config);
            out.write(val.getBytes());
        } else {
            // 走不到这里
            out.write(MARKS);
            out.write(value.toString().getBytes());
            out.write(MARKS);
        }
    }

}

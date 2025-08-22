package cn.chenzhen.wj.json;

import java.io.*;
import java.math.BigDecimal;

public class JsonTokener implements AutoCloseable {
    private static final int EOF = (byte) -1;
    private static final int COMMA = ',';
    private static final int COLON = ':';
    private static final int MARKS = '"';
    private static final int BACKSLASH = '\\';
    private static final int EMPTY = ' ';
    private static final int TAB = '\t';
    private static final int LF = '\n';
    private static final int JSON_TAG_CB_START = '{';
    private static final int JSON_TAG_CB_END = '}';
    private static final int JSON_TAG_SB_START = '[';
    private static final int JSON_TAG_SB_END = ']';

    private Reader reader;
    private int currentCode;
    private int previousCode;
    private boolean back;
    /**
     * 当前行
     */
    private int currentRow = 1;
    /**
     * 当前列
     */
    private int currentColumn = 0;

    public JsonTokener(String json) {
        reader = new StringReader(json);
    }

    public JsonTokener(InputStream is) {
        this.reader = new InputStreamReader(is);
    }

    /**
     * 解析json字符串
     * @return json对象
     */
    public Object parse() {
        int code = nextToken();
        back();
        if (code == JSON_TAG_CB_START) {
            return parseJsonObject();
        } else if (code == JSON_TAG_SB_START) {
            return parseJsonArray();
        }
        throw error("Illegal symbols " + (char) code);
    }

    /**
     * 将字符串解析为 json对象
     * @return json对象
     */
    private JsonObject parseJsonObject() {
        JsonObject json = new JsonObject();
        int code = nextToken();
        assertCode(code, JSON_TAG_CB_START);
        code = nextToken();
        if (code == JSON_TAG_CB_END) {
            return json;
        }
        back();
        while (true) {
            code = nextToken();
            assertCode(code, MARKS);
            // 读取 JSON key
            String key = parseJsonStr(MARKS);
            code = nextToken();
            assertCode(code, COLON);
            Object value = parseJsonValue();
            json.put(key, value);
            code = nextToken();
            if (code == JSON_TAG_CB_END) {
                break;
            }
            assertCode(code, COMMA);
        }
        return json;
    }

    /**
     * 将字符串解析为json数组
     * @return json数组
     */
    private JsonArray parseJsonArray() {
        JsonArray array = new JsonArray();
        int code = nextToken();
        assertCode(code, JSON_TAG_SB_START);

        code = nextToken();
        // 空数组
        if (code == JSON_TAG_SB_END) {
            return array;
        }
        back();

        while (true) {
            Object val = parseJsonValue();
            array.add(val);
            code = nextToken();
            if (code == JSON_TAG_SB_END) {
                break;
            }
            assertCode(code, COMMA);

        }
        return array;
    }

    /**
     * 解析json的值
     * @return 值
     */
    private Object parseJsonValue(){
        int code = nextToken();
        back();
        switch (code) {
            // JSON对象
            case JSON_TAG_CB_START:
                return parseJsonObject();
            // json 数组
            case JSON_TAG_SB_START:
                return parseJsonArray();
            // JSON字符串
            case MARKS:
                next();
                return parseJsonStr(MARKS);
            // null、 true、false、数字
            default:
                String value = parseJsonStr(COMMA);
                back();
                return simpleValueConvert(value);
        }
    }

    /**
     * 普通值转换
     * @param val 值
     * @return java对象值
     */
    private Object simpleValueConvert(String val){
        // null、 true、false、数字
        if ("true".equals(val)) {
            return true;
        } else if ("false".equals(val)) {
            return false;
        } else if ("null".equals(val)) {
            return null;
        }
        try {
            return new BigDecimal(val.trim());
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * 读取字符串 到指定为止结束
     * @param end 结束符号
     * @return 字符串
     */
    private String parseJsonStr(int end) {
        StringBuilder key = new StringBuilder();
        while (true) {
            int code = next();
            if (previousCode != BACKSLASH && code == end) {
                break;
            }
            if (end == COMMA && (code == JSON_TAG_SB_END || code == JSON_TAG_CB_END)) {
                break;
            }
            if (code == EOF) {
                throw error("error json format");
            }
            key.append((char) code);
        }
        String val = key.toString();
        // 转义符号去除
        val = val.replace("\\\"", "\"");
        return val;
    }

    private void back() {
        if (back) {
            throw new JsonException("Already rolled back");
        }
        currentColumn--;
        back = true;
    }

    private int nextToken() {
        while (true) {
            int code = next();
            switch (code) {
                case EMPTY:
                case LF:
                case TAB:
                    continue;
                default:
                    return code;

            }
        }
    }

    private int next() {
        try {
            if (back) {
                back = false;
                currentColumn++;
                return currentCode;
            }
            previousCode = currentCode;
            int code = reader.read();
            if (code == LF) {
                currentRow++;
                currentColumn = 0;
            } else {
                currentColumn++;
            }
            currentCode = code;
            return code;
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
    private void assertCode(int code, int expected){
        if (code != expected) {
            throw error( "Expected " + (char)expected + " but got " + (char)code);
        }
    }
    private JsonException error(String msg){
        throw new JsonException("row " + currentRow + " column " + currentColumn + ": " + msg );
    }
}
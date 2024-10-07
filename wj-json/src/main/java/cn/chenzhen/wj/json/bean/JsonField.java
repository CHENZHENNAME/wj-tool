package cn.chenzhen.wj.json.bean;

import cn.chenzhen.wj.json.JsonObject;

public class JsonField {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段值 反序列化时为空 需要从 jsonObject 中获取
     */
    private Object value;
    /**
     * 字段值类型
     */
    private Class<?> type;
    /**
     * 是否已经处理过字段值
     * false 没处理 后续程序继续处理
     * true 已经处理 后续不再继续处理
     */
    private boolean flag = false;
    /**
     * 是否忽略此属性
     */
    private boolean ignore = false;
    /**
     * 解包 序列化 反序列化时使用
     */
    private boolean unwrappedFlag = false;
    /**
     * 反序列化时使用：JsonObject、JsonArray
     */
    private JsonObject jsonObject;

    public JsonField(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public JsonField(String name, Class<?> type, JsonObject jsonObject) {
        this.name = name;
        this.type = type;
        this.jsonObject = jsonObject;
    }

    public JsonField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isUnwrappedFlag() {
        return unwrappedFlag;
    }

    public void setUnwrappedFlag(boolean unwrappedFlag) {
        this.unwrappedFlag = unwrappedFlag;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}

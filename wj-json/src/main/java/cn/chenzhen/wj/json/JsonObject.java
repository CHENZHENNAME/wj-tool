package cn.chenzhen.wj.json;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JSON属性
 */
public class JsonObject {
    /**
     * JSON属性
     */
    private Map<String, Object> data = new LinkedHashMap<>();

    /**
     * 获取指定属性的值
     * @param key 属性
     * @return 值
     */
    public Object get(String key) {
        return data.get(key);
    }

    /**
     * 设置指定属性的值
     * @param key 属性
     * @param value 值
     */
    public void set(String key, Object value) {
        data.put(key, value);
    }

    /**
     * 删除指定属性
     * @param key 属性
     * @return 值
     */
    public Object remove(String key) {
        return data.remove(key);
    }
    /***
     * 获取所有属性
     * @return 属性
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * 设置属性 会覆盖原来的数据
     * @param data 属性
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    /**
     * 设置指定属性的值
     * @param key 属性
     * @param value 值
     */
    public void put(String key, Object value) {
        data.put(key, value);
    }
}

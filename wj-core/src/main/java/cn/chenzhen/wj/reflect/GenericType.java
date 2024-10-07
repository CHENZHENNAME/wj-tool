package cn.chenzhen.wj.reflect;

import java.util.HashMap;
import java.util.Map;

/**
 * 泛型解析数 实体类
 */
public class GenericType {
    /**
     * 当前类上的泛型信息
     */
    private Map<String, GenericType> genericType = new HashMap<>();
    /***
     * 当前类的类型
     */
    private Class<?> type;

    public GenericType() {
    }

    public GenericType(Class<?> type) {
        this.type = type;
    }

    public Map<String, GenericType> getGenericType() {
        return genericType;
    }

    public void setGenericType(Map<String, GenericType> genericType) {
        this.genericType = genericType;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void put(String name, GenericType type) {
        genericType.put(name, type);
    }
}

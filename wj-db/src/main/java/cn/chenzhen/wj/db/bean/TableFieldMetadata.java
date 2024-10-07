package cn.chenzhen.wj.db.bean;


/**
 * 字段注解元数据
 */
public class TableFieldMetadata {
    /**
     * 字段名称
     */
    private final String name;
    /**
     * 是否为主键
     */
    private final boolean primaryKey;

    /**
     * 字段值
     */
    private Object value;

    public TableFieldMetadata(String name, boolean primaryKey, Object value) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.value = value;
    }

    public TableFieldMetadata(String name, Object value) {
        this.name = name;
        this.primaryKey = false;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public Object getValue() {
        return value;
    }
}

package cn.chenzhen.wj.xml.bean;

import cn.chenzhen.wj.xml.XmlObject;

public class XmlField {
    /**
     * 标签名称
     */
    private String name;
    /**
     * 序列化时 字段的值 注意反序列化时为空
     */
    private Object value;
    /**
     * 字段类型
     */
    private Class<?> type;
    /**
     * 当前节点
     */
    private XmlObject node;
    /**
     * 父节点
     */
    private XmlObject parent;
    /**
     * 是否忽略此属性
     */
    private boolean ignore = false;
    /**
     * 是否已经处理过字段值
     * false 没处理 后续程序继续处理
     * true 已经处理 后续不再继续处理
     */
    private boolean flag = false;

    public XmlField(String name, XmlObject node) {
        this.name = name;
        this.node = node;
    }

    public XmlField(String name, Object value, XmlObject node) {
        this.name = name;
        this.value = value;
        this.node = node;
    }

    public XmlField(String name, XmlObject node, XmlObject parent) {
        this.name = name;
        this.node = node;
        this.parent = parent;
    }

    public XmlField(String name, Object value, XmlObject node, XmlObject parent) {
        this.name = name;
        this.value = value;
        this.node = node;
        this.parent = parent;
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

    public XmlObject getNode() {
        return node;
    }

    public void setNode(XmlObject node) {
        this.node = node;
    }

    public XmlObject getParent() {
        return parent;
    }

    public void setParent(XmlObject parent) {
        this.parent = parent;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}

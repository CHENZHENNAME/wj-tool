package cn.chenzhen.wj.xml;

import java.util.*;

/**
 * Xml节点
 */
public class XmlObject {
    /**
     * Xml节点属性
     */
    private Map<String, String> attributes;
    /**
     * Xml子节点
     */
    private Map<String, Object> nodes;
    /**
     * Xml节点值 如果有多个 最后一个会覆盖前面的
     */
    private Object value;

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    public void appendAttribute(String key, String value) {
        if (attributes == null) {
            attributes = new LinkedHashMap<>();
        }
        attributes.put(key, value);
    }
    public Object getAttribute(String key){
        return attributes.get(key);
    }

    public Map<String, Object> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, Object> nodes) {
        this.nodes = nodes;
    }
    public Object getNode(String key) {
        return nodes.get(key);
    }
    public Map.Entry<String, Object> getFirstNode() {
        if (nodes == null) {
            return null;
        }
        Set<Map.Entry<String, Object>> entrySet = nodes.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        return iterator.next();
    }
    @SuppressWarnings("unchecked")
    public void appendNode(String key, Object value) {
        if (nodes == null) {
            nodes = new LinkedHashMap<>();
        }
        Object xml = nodes.get(key);
        if (xml != null) {
            // 已经存在
            if (xml instanceof List) {
                ((List<Object>)xml).add(value);
            } else {
                LinkedList<Object> list = new LinkedList<>();
                list.add(xml);
                list.add(value);
                nodes.put(key, list);
            }
            return;
        }
        nodes.put(key, value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

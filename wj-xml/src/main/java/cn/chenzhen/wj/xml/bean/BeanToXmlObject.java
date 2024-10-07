package cn.chenzhen.wj.xml.bean;

import cn.chenzhen.wj.reflect.ClassUtil;
import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeUtil;
import cn.chenzhen.wj.xml.XmlConfig;
import cn.chenzhen.wj.xml.XmlObject;
import cn.chenzhen.wj.xml.bean.annotation.Attribute;
import cn.chenzhen.wj.xml.bean.annotation.Xml;
import cn.chenzhen.wj.xml.bean.annotation.processor.AnnotationProcessor;
import cn.chenzhen.wj.xml.bean.annotation.processor.AnnotationProcessorFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanToXmlObject {
    private XmlConfig config;

    public BeanToXmlObject() {
        this(XmlConfig.global());
    }

    public BeanToXmlObject(XmlConfig config) {
        this.config = config;
    }
    public XmlObject toXmlObject(Object bean) {
        if (bean == null) {
            return null;
        }
        if (bean instanceof XmlObject) {
            return (XmlObject) bean;
        }
        XmlObject root = new XmlObject();
        Class<?> cls = bean.getClass();
        XmlObject parent = parseXmlAnnotation(null, cls, root);
        if (parent == root) {
            String clsSimpleName = cls.getSimpleName();
            toXmlObject(parent, clsSimpleName, bean);

        }else {
            toXmlObject(parent, null, bean);
        }

        return root;

    }
    private void toXmlObject(XmlObject parent, String tageName, Object bean) {

        if (bean == null) {
            if (tageName == null || tageName.isEmpty()) {
                return;
            }
            // 只有一个标签的情况
            parent.appendNode(tageName, null);
        }

        if (bean instanceof XmlObject) {
            parent.appendNode(tageName, bean);
        }
        Class<?> cls = bean.getClass();
        parent = parseXmlAnnotation(tageName, cls, parent);


        // 处理字段
        List<Method> list = ClassUtil.getterMethods(cls);
        for (Method method : list) {
            XmlObject fieldNode = new XmlObject();
            List<Annotation> fieldAnn = ClassUtil.getFieldAndMethodAnnotations(method);
            Object val = ClassUtil.invoke(bean, method);
            String name = ClassUtil.getFieldName(method);
            XmlField nodeField = new XmlField(name, val, fieldNode, parent);
            // 处理字段上的注解
            processor(fieldAnn, nodeField);
            if (nodeField.isIgnore()) {
                continue;
            }
            XmlObject node = nodeField.getNode();
            parent.appendNode(nodeField.getName(), node);
            if (nodeField.isFlag()) {
                node.setValue(fieldNode.getValue());
            } else {
                fieldValueToXmlObject(node, nodeField.getName(), val);
            }

        }

    }
    private void fieldValueToXmlObject(XmlObject parent, String tageName, Object bean) {
        if (bean == null) {
            return;
        }
        // 基本类型处理
        Object val = TypeUtil.serialize(bean);
        if (val != null) {
            parent.setValue(val);
            return;
        }
        Class<?> cls = bean.getClass();
        // 日期处理
        if (DateUtil.getConvertService(cls) != null) {
            // 日期类型
            parent.setValue(bean);
            return;
        }
        objectToXmlObject(parent, tageName, bean);
    }

    /**
     * 字段值处理
     * @param parent 父节点
     * @param tageName 标签名称
     * @param bean 节点值
     */
    private void valueToXmlObject(XmlObject parent,String tageName, Object bean) {
        if (bean == null) {
            parent.appendNode(tageName, null);
            return;
        }
        // 基本类型处理
        Object val = TypeUtil.serialize(bean);
        if (val != null) {
            parent.appendNode(tageName, val);
            return;
        }
        Class<?> cls = bean.getClass();
        // 日期处理
        if (DateUtil.getConvertService(cls) != null) {
            // 日期类型
            parent.appendNode(tageName, bean);
            return;
        }
        objectToXmlObject(parent, tageName, bean);
    }
    private void objectToXmlObject(XmlObject parent, String tageName, Object bean) {
        Class<?> cls = bean.getClass();
        if (cls.isArray()) {
            arrayToXmlArray(parent, tageName, bean);
        } else if (Iterable.class.isAssignableFrom(cls)) {
            iterableToXmlArray(parent, tageName,(Iterable<?>) bean);
        } else if (Map.class.isAssignableFrom(cls)) {
            XmlObject object = new XmlObject();
            parent.appendNode(tageName, object);
            mapToXmlObject(object, (Map<?, ?>) bean);
        } else {
            toXmlObject(parent, tageName, bean);
        }
    }
    private void mapToXmlObject(XmlObject parent, Map<?, ?> bean) {
        Set<? extends Map.Entry<?, ?>> entriedSet = bean.entrySet();
        for (Map.Entry<?, ?> entry : entriedSet) {
            valueToXmlObject(parent, entry.getKey().toString(), entry.getValue());
        }
    }

    private void iterableToXmlArray(XmlObject parent, String tageName, Iterable<?> bean) {
        for (Object val : bean) {
            valueToXmlObject(parent, tageName, val);
        }
    }

    private void arrayToXmlArray(XmlObject parent, String tageName, Object array) {
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object val = Array.get(array, i);
            valueToXmlObject(parent, tageName, val);
        }
    }

    private void processor(List<Annotation> ann, XmlField field){
        for (Annotation annotation : ann) {
            AnnotationProcessor<Annotation> processor = AnnotationProcessorFactory.getAnnotationProcessor(annotation);
            if (processor == null) {
                continue;
            }
            processor.serialize(annotation, field, config);
        }
    }

    /**
     * 处理类上的 xml注解
     * @param tageName 上级标签名称
     * @param type 类
     * @param parent 父节点
     * @return 处理后的节点
     */
    private XmlObject parseXmlAnnotation(String tageName, Class<?> type, XmlObject parent){
        XmlObject nodeParent = parent;
        // 上级标签不为空
        if (tageName != null && !tageName.isEmpty()) {
            nodeParent = new XmlObject();
            parent.appendNode(tageName, nodeParent);
        }
        XmlObject object = new XmlObject();
        String clsSimpleName = type.getSimpleName();
        Xml xmlAnn = type.getAnnotation(Xml.class);
        if (xmlAnn != null) {
            if (!xmlAnn.value().isEmpty()) {
                clsSimpleName = xmlAnn.value();
            }
            Attribute[] attribute = xmlAnn.attribute();
            for (Attribute att : attribute) {
                object.appendAttribute(att.attrName(), att.attrValue());
            }

            nodeParent.appendNode(clsSimpleName, object);
            return object;
        } else {
            return nodeParent;
        }

    }
}

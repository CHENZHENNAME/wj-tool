package cn.chenzhen.wj.xml.bean;

import cn.chenzhen.wj.reflect.ClassUtil;
import cn.chenzhen.wj.reflect.GenericType;
import cn.chenzhen.wj.reflect.TypeReference;
import cn.chenzhen.wj.type.BeanClassTypeFactory;
import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeUtil;
import cn.chenzhen.wj.type.convert.service.date.ConvertService;
import cn.chenzhen.wj.xml.XmlConfig;
import cn.chenzhen.wj.xml.XmlException;
import cn.chenzhen.wj.xml.XmlObject;
import cn.chenzhen.wj.xml.bean.annotation.Xml;
import cn.chenzhen.wj.xml.bean.annotation.processor.AnnotationProcessor;
import cn.chenzhen.wj.xml.bean.annotation.processor.AnnotationProcessorFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class XmlObjectToBean {
    private XmlConfig config;
    public XmlObjectToBean() {
        this(XmlConfig.global());
    }
    public XmlObjectToBean(XmlConfig config) {
        this.config = config;
    }
    public <T> T toBean(XmlObject xml, Class<T> clazz) {
        return toBean(xml, ClassUtil.typeReference(clazz));
    }
    public <T> T toBean(XmlObject xml, TypeReference<T> typeReference) {
        GenericType type = typeReference.getGenericType();
        XmlObject rootNode = getRootNode(xml, type.getType());
        return toBean(rootNode, type);
    }
    @SuppressWarnings("unchecked")
    public <T> T toBean(XmlObject parent, GenericType type) {
        Class<?> classType = type.getType();
        Object bean = BeanClassTypeFactory.createBean(classType);
        List<Method> list = ClassUtil.setterMethods(classType);
        for (Method method : list) {
            String name = ClassUtil.getFieldName(method);

            // 获取方法参数类型
            Type pt = method.getGenericParameterTypes()[0];
            GenericType targetType = null;
            if (pt instanceof TypeVariable) {
                // 泛型丢失 尝试修复
                targetType = type.getGenericType().get(pt.getTypeName());
            }
            if (targetType == null) {
                // 当前Type上也可能带有泛型需要进行解析
                targetType = ClassUtil.typeReference(pt).getGenericType();
            }

            XmlField field = new XmlField(name, null, parent);
            field.setType(targetType.getType());
            List<Annotation> ann = ClassUtil.getFieldAndMethodAnnotations(method);
            annotationsProcessor(ann, field);
            if (field.isIgnore()) {
                continue;
            }
            if (field.isFlag()){
                ClassUtil.invoke(bean, method, field.getValue());
                continue;
            }
            name = field.getName();
            Object node = parent.getNode(name);
            Object value = null;
            if (pt instanceof GenericArrayType) {
                // 泛型数组
                value = xmlObjectToArray(node, targetType);
            } else {
                value = xmlValueConvert(node, targetType);
            }
            ClassUtil.invoke(bean, method, value);
        }
        return (T)bean;
    }

    private Object xmlObjectToArray(Object node, GenericType targetType) {
        if (node == null) {
            return null;
        }
        Class<?> type = targetType.getType();
        if (type.isArray()) {
            type = type.getComponentType();
        }
        // 因为 xml标签可以重复出现
        // 可能只有一个普通值 也可能是一个复杂对象值
        if (node instanceof List) {
            List<?> list = (List<?>) node;
            int size = list.size();
            Object bean = Array.newInstance(type, size);
            for (int i = 0; i < size; i++) {
                Object item = list.get(i);
                Object val = xmlValueConvert(item, targetType);
                Array.set(bean, i, val);
            }
            return bean;
        } else {
            // 单个标签 转换为数组
            Object bean = Array.newInstance(type, 1);
            Object val = xmlValueConvert(node, targetType);
            Array.set(bean, 0, val);
            return bean;
        }
    }
    @SuppressWarnings("unchecked")
    private Object xmlObjectToCollection(Object node, GenericType targetType) {
        Class<?> type = targetType.getType();
        String typeName = type.getTypeParameters()[0].getTypeName();
        GenericType genericType = targetType.getGenericType().get(typeName);

        List<Object> bean = (List<Object>)BeanClassTypeFactory.createBean(type);
        // 泛型丢失
        if (genericType == null || genericType.getType() == Object.class) {
            if (node instanceof List) {
                return node;
            }
            // 单个标签的情况
            bean.add(node);
            return bean;
        }
        // 因为 xml标签可以重复出现
        // 可能只有一个普通值 也可能是一个复杂对象值
        if (node instanceof List) {
            List<?> list = (List<?>) node;
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Object item = list.get(i);
                Object val = xmlValueConvert(item, targetType);
                Array.set(bean, i, val);
            }
            return bean;
        } else {
            // 单个标签 转换为集合
            Object val = xmlValueConvert(node, targetType);
            bean.add(val);
        }
        return bean;
    }

    private Object xmlValueConvert(Object node, GenericType targetType) {
        if (node == null) {
            return null;
        }
        Class<?> type = targetType.getType();
        // node 可能是 基本类型 也可能是 XmlObject
        // 基本类型
        Object nodeValue = null;
        Object val = null;
        if (node instanceof XmlObject) {
            nodeValue = ((XmlObject) node).getValue();
            if (nodeValue == null) {
                return null;
            }
            val = TypeUtil.deserializer(nodeValue, type);
        } else {
            val = TypeUtil.deserializer(node, type);
        }
        if (val != null) {
            return val;
        }
        // 日期类型
        ConvertService<?> service = DateUtil.getConvertService(type);
        if (service != null) {
            return service.deserializer(nodeValue, config.getPattern(type));
        }
        // 自定义对象类型
        if (type.isArray()) {
            xmlObjectToArray(node, targetType);
        }
        if (Collection.class.isAssignableFrom(type)) {
            return xmlObjectToCollection(node, targetType);
        }
        if (Map.class.isAssignableFrom(type)) {
            if (node instanceof XmlObject) {
                return ((XmlObject) node).getNodes();
            }
            throw new XmlException("error type " + type.getName() + " not supported to convert to Map");
        }
        if (node instanceof XmlObject) {
            return toBean((XmlObject) node, targetType);
        }
        throw new XmlException("error type " + node.getClass() + " not supported to convert to " + type);
    }



    private void annotationsProcessor(List<Annotation> annotations, XmlField field){
        for (Annotation ann : annotations) {
            AnnotationProcessor<Annotation> processor = AnnotationProcessorFactory.getAnnotationProcessor(ann);
            if (processor == null) {
                continue;
            }
            processor.deserializer(ann, field, config);
        }
    }
    private XmlObject getRootNode(XmlObject xml, Class<?> clazz){
        Xml ann = clazz.getAnnotation(Xml.class);
        String name = clazz.getSimpleName();

        if (ann != null && !ann.value().isEmpty()){
            name = ann.value();
        }
        Object node = xml.getNode(name);
        if (node == null) {
            // 都没有的情况下尝试使用第一个标签
            node = xml.getFirstNode().getValue();
        }

        if (node instanceof XmlObject){
            return (XmlObject) node;
        }
        throw new XmlException("error type " + node.getClass());
    }
}

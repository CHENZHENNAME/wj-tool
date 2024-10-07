package cn.chenzhen.wj.xml.bean.annotation.processor;

import cn.chenzhen.wj.xml.XmlConfig;
import cn.chenzhen.wj.xml.bean.XmlField;

public interface AnnotationProcessor<T> {
    /**
     * 序列化处理Xml字段
     * @param ann 注解信息
     * @param field 字段信息
     * @param config 配置信息
     */
    void serialize(T ann, XmlField field, XmlConfig config);

    /**
     * 反序列化处理Xml字段
     * @param ann 注解信息
     * @param field 字段信息
     * @param config 配置信息
     */
    void deserializer(T ann, XmlField field, XmlConfig config);

}

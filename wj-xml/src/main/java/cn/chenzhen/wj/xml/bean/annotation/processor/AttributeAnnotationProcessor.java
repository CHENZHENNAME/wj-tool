package cn.chenzhen.wj.xml.bean.annotation.processor;

import cn.chenzhen.wj.xml.XmlConfig;
import cn.chenzhen.wj.xml.XmlObject;
import cn.chenzhen.wj.xml.bean.XmlField;
import cn.chenzhen.wj.xml.bean.annotation.Attribute;

public class AttributeAnnotationProcessor implements AnnotationProcessor<Attribute>{
    @Override
    public void serialize(Attribute ann, XmlField field, XmlConfig config) {
        XmlObject node = field.getNode();
        node.appendAttribute(ann.attrName(), ann.attrValue());
    }

    @Override
    public void deserializer(Attribute ann, XmlField field, XmlConfig config) {

    }
}

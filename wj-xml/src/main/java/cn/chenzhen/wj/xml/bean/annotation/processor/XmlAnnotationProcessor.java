package cn.chenzhen.wj.xml.bean.annotation.processor;

import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.xml.XmlConfig;
import cn.chenzhen.wj.xml.XmlException;
import cn.chenzhen.wj.xml.XmlObject;
import cn.chenzhen.wj.xml.bean.XmlField;
import cn.chenzhen.wj.xml.bean.annotation.Attribute;
import cn.chenzhen.wj.xml.bean.annotation.Xml;

import java.util.Collection;

public class XmlAnnotationProcessor implements AnnotationProcessor<Xml>{
    @Override
    public void serialize(Xml ann, XmlField field, XmlConfig config) {
        if (ann.ignore()) {
            field.setIgnore(true);
            return;
        }
        if (!ann.value().isEmpty()){
            field.setName(ann.value());
        }
        Attribute[] attribute = ann.attribute();
        XmlObject node = field.getNode();
        for (Attribute attr : attribute){
            node.appendAttribute(attr.attrName(), attr.attrValue());
        }
        if (!ann.pattern().isEmpty()) {
            String date = DateUtil.format(field.getValue(), ann.pattern());
            field.setValue(date);
            field.setFlag(true);
        }
    }

    @Override
    public void deserializer(Xml ann, XmlField field, XmlConfig config) {
        if (!ann.value().isEmpty()){
            field.setName(ann.value());
        }
        String pattern = ann.pattern();
        if (!pattern.isEmpty()) {
            Class<?> type = field.getType();
            // 日期处理
            Object date = field.getParent().getNode(field.getName());
            if (date instanceof Collection) {
                throw new XmlException("array is not supported");
            }
            date = DateUtil.parse(date, pattern, type);
            field.setValue(date);
        }
    }
}

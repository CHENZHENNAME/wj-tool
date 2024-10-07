package cn.chenzhen.wj.xml.bean.annotation.processor;

import cn.chenzhen.wj.type.convert.TypeUtil;
import cn.chenzhen.wj.xml.XmlConfig;
import cn.chenzhen.wj.xml.bean.XmlField;
import cn.chenzhen.wj.xml.bean.annotation.Attr;

public class AttrAnnotationProcessor implements AnnotationProcessor<Attr>{
    @Override
    public void serialize(Attr ann, XmlField field, XmlConfig config) {
        String name = ann.value();
        if (name.isEmpty()) {
            name = field.getName();
        }
        Object value = field.getValue();
        Object val = TypeUtil.serialize(value);
        if (val == null) {
            val = "";
        }

        field.getParent().appendAttribute(name, val.toString());
        field.setIgnore(true);
    }

    @Override
    public void deserializer(Attr ann, XmlField field, XmlConfig config) {
        String name = ann.value();
        if (name.isEmpty()) {
            name = field.getName();
        }
        Object value = field.getParent().getAttribute(name);
        field.setValue(value);
        field.setFlag(true);
    }
}

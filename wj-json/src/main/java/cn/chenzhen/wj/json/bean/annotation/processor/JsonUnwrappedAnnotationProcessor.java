package cn.chenzhen.wj.json.bean.annotation.processor;

import cn.chenzhen.wj.json.*;
import cn.chenzhen.wj.json.bean.JsonField;
import cn.chenzhen.wj.json.bean.annotation.JsonUnwrapped;



public class JsonUnwrappedAnnotationProcessor implements AnnotationProcessor<JsonUnwrapped>{
    @Override
    public void serialize(JsonUnwrapped ann, JsonField field, JsonConfig config) {
        field.setIgnore(true);
        field.setUnwrappedFlag(true);
    }

    @Override
    public void deserializer(JsonUnwrapped ann, JsonField field, JsonConfig config) {
        field.setFlag(true);
        field.setUnwrappedFlag(true);
    }

}

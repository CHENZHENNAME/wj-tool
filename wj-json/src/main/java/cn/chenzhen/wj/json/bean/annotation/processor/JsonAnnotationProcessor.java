package cn.chenzhen.wj.json.bean.annotation.processor;

import cn.chenzhen.wj.json.JsonArray;
import cn.chenzhen.wj.json.JsonConfig;
import cn.chenzhen.wj.json.JsonException;
import cn.chenzhen.wj.json.bean.JsonField;
import cn.chenzhen.wj.json.bean.annotation.Json;
import cn.chenzhen.wj.type.BeanClassTypeFactory;
import cn.chenzhen.wj.type.convert.DateUtil;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

public class JsonAnnotationProcessor implements AnnotationProcessor<Json>{
    @Override
    public void serialize(Json ann, JsonField field, JsonConfig config) {
        if (ann.ignore()){
            field.setIgnore(true);
        }
        if (!ann.value().isEmpty()) {
            field.setName(ann.value());
        }
        // 日期处理
        if(!ann.pattern().isEmpty()) {
            String val = DateUtil.format(field.getValue(), ann.pattern());
            field.setValue(val);
            field.setFlag(true);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deserializer(Json ann, JsonField field, JsonConfig config) {
        if (ann.ignore()) {
            field.setIgnore(true);
            return;
        }
        if (!ann.value().isEmpty()) {
            field.setName(ann.value());
        }

        if (!ann.pattern().isEmpty()) {
            Object val = field.getJsonObject().get(field.getName());
            if (val == null) {
                return;
            }
            deserializerDate(field, val, ann.pattern());
        }
    }

    /**
     * 指定日期格式的日期处理
     * @param field 字段信息
     * @param val 值
     * @param pattern 格式
     */
    private void deserializerDate(JsonField field, Object val, String pattern){

        Class<?> type = field.getType();
        Object date = null;
        // 数组或者集合日期对象
        if (val instanceof JsonArray) {
           throw new JsonException("array is not supported");
        } else {
            // 单个日期对象
            date = DateUtil.parse(val, pattern, type);
        }
        field.setValue(date);
        field.setFlag(true);
    }
}

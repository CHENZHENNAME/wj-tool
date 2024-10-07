package cn.chenzhen.wj.json.bean.annotation.processor;

import cn.chenzhen.wj.json.JsonConfig;
import cn.chenzhen.wj.json.bean.JsonField;

public interface AnnotationProcessor<T> {
    /**
     * 序列化处理Json字段
     * @param ann 注解信息
     * @param field 字段信息
     * @param config 配置信息
     */
    void serialize(T ann, JsonField field, JsonConfig config);

    /**
     * 反序列化处理Json字段
     * @param ann 注解信息
     * @param field 字段信息
     * @param config 配置信息
     */
    void deserializer(T ann, JsonField field, JsonConfig config);

}

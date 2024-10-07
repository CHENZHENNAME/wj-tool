package cn.chenzhen.wj.json.bean;

import cn.chenzhen.wj.json.JsonArray;
import cn.chenzhen.wj.json.JsonConfig;
import cn.chenzhen.wj.json.JsonException;
import cn.chenzhen.wj.json.JsonObject;
import cn.chenzhen.wj.json.bean.annotation.processor.AnnotationProcessor;
import cn.chenzhen.wj.json.bean.annotation.processor.AnnotationProcessorFactory;
import cn.chenzhen.wj.reflect.ClassUtil;
import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeUtil;
import cn.chenzhen.wj.type.convert.service.date.ConvertService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

/**
 * java对象转换为 json对象
 */
public class BeanToJsonObject {

    /**
     * java对象转换为Json对象
     * @param bean java对象
     * @return json对象：JsonObject、JsonArray
     */
    public static Object toJsonObject(Object bean) {
        return toJsonObject(bean, JsonConfig.global());
    }

    /**
     * java对象转换为Json对象
     * @param bean java对象
     * @param jsonConfig 配置文件
     * @return json对象：JsonObject、JsonArray
     */
    public static Object toJsonObject(Object bean, JsonConfig jsonConfig) {
        if (bean == null){
            return null;
        }
        Class<?> cls = bean.getClass();
        if (TypeUtil.serialize(bean) != null || DateUtil.getConvertService(cls) != null) {
            // 基本类型
            throw new JsonException("基本类型无法转换为json对象");
        }
        if (cls.isArray()) {
            return arrayToJsonArray(bean, jsonConfig);
        } else if (Iterable.class.isAssignableFrom(cls)) {
            return iterableToJsonArray((Iterable<?>) bean, jsonConfig);
        } else if (Map.class.isAssignableFrom(cls)) {
            return mapToJsonObject((Map<?, ?>) bean, jsonConfig);
        } else {
            return beanToJsonObject(bean, jsonConfig);
        }
    }

    /**
     * 集合对象转换为 JsonArray
     * @param itr 集合
     * @param jsonConfig 配置文件
     * @return JsonArray对象
     */
    private static JsonArray iterableToJsonArray(Iterable<?> itr, JsonConfig jsonConfig) {
        JsonArray json = new JsonArray();
        for (Object item : itr) {
            Object val = jsonValueConvert(item, jsonConfig);
            json.add(val);
        }
        return json;
    }

    /**
     * 集合对象转换为 JsonObject
     * @param map 集合
     * @param jsonConfig 配置文件
     * @return JsonObject对象
     */
    private static JsonObject mapToJsonObject(Map<?,?> map, JsonConfig jsonConfig) {
        JsonObject json = new JsonObject();
        Set<? extends Map.Entry<?, ?>> entriedSet = map.entrySet();
        for (Map.Entry<?, ?> entry : entriedSet) {
            String key = entry.getKey().toString();
            Object val = jsonValueConvert(entry.getValue(), jsonConfig);
            json.put(key, val);
        }
        return json;
    }

    /**
     * 数组对象转换为 JsonArray
     * @param array 数组
     * @param jsonConfig 配置文件
     * @return JsonArray对象
     */
    private static JsonArray arrayToJsonArray(Object array, JsonConfig jsonConfig) {
        JsonArray json = new JsonArray();
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(array, i);
            Object val = jsonValueConvert(item, jsonConfig);
            json.add(val);
        }
        return json;
    }


    /**
     * java对象转换为 JsonObject
     * @param bean java对象
     * @param jsonConfig 配置文件
     * @return JsonObject对象
     */
    private static JsonObject beanToJsonObject(Object bean, JsonConfig jsonConfig) {
        if (bean == null){
            return null;
        }
        JsonObject json = new JsonObject();
        List<Method> methods = ClassUtil.getterMethods(bean.getClass());
        for (Method method : methods) {
            // 注解信息获取
            List<Annotation> annList = ClassUtil.getFieldAndMethodAnnotations(method);
            Object result = ClassUtil.invoke(bean, method);
            String name = ClassUtil.getFieldName(method);
            JsonField field = new JsonField(name, result);
            annotationProcessor(field, annList, jsonConfig);
            if (field.isIgnore()) {
                // 解包反序列化特殊处理 不在外围判断提高效率
                if (field.isUnwrappedFlag()) {
                    Object val = jsonValueConvert(result, jsonConfig);
                    unwrappedField(json, val);
                }
                continue;
            }
            if (!field.isFlag()) {

                field.setValue(jsonValueConvert(result, jsonConfig));
            }
            json.put(field.getName(), field.getValue());
        }
        return json;
    }

    /**
     * 序列化时 解包从挨揍
     * @param json json对象
     * @param val 需要解包的对象
     */
    private static void unwrappedField(JsonObject json, Object val){
        if (val == null) {
            return;
        }
        if (val instanceof JsonArray) {
            throw new JsonException("Collections and arrays unable unwrapped");
        }
        if (!(val instanceof JsonObject)) {
            throw new JsonException("Unsupported types " + val.getClass());
        }
        Map<String, Object> data = ((JsonObject) val).getData();
        Set<Map.Entry<String, Object>> entrySet = data.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            json.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Json值转换
     * @param data 值
     * @param jsonConfig 配置文件
     * @return 转换后的值
     */
    private static Object jsonValueConvert(Object data, JsonConfig jsonConfig){
        if (data == null) {
            return null;
        }
        // 数字类型
        if (data instanceof Number) {
            return data;
        }
        // 基本类型
        Object val = TypeUtil.serialize(data);
        if (val != null ){
            return val;
        }
        ConvertService<?> service = DateUtil.getConvertService(data.getClass());
        if (service!= null) {
            // 日期类型
            return data;
        }
        // 其他自定义对象
        return toJsonObject(data, jsonConfig);

    }

    /**
     * 处理注解
     * @param field json字段
     * @param annList 注解列表
     * @param jsonConfig 配置信息
     */
    private static void annotationProcessor(JsonField field, List<Annotation> annList, JsonConfig jsonConfig) {
        for (Annotation ann : annList) {
            AnnotationProcessor<Annotation> processor = AnnotationProcessorFactory.getAnnotationProcessor(ann);
            if (processor ==  null) {
                continue;
            }
            processor.serialize(ann, field, jsonConfig);
        }
    }
}

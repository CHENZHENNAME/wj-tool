package cn.chenzhen.wj.json;

import cn.chenzhen.wj.json.bean.BeanToJsonObject;
import cn.chenzhen.wj.json.bean.JsonObjectToBean;
import cn.chenzhen.wj.reflect.TypeReference;

import java.io.IOException;

public class JsonUtil {
    /**
     * json字符串反序列化为java对象
     * @param json json字符串
     * @param clazz 目标类型
     * @return 目标对象
     */
    public static <T> T jsonToBean(String json, Class<T> clazz) {
        return jsonToBean(json, clazz, JsonConfig.global());
    }

    /**
     * json字符串反序列化为java对象
     * @param json json字符串
     * @param clazz 目标类型
     * @param config 凭证
     * @return 目标对象
     */
    public static <T> T jsonToBean(String json, Class<T> clazz, JsonConfig config) {
        return JsonObjectToBean.jsonToBean(json, clazz, config);
    }
    /**
     * json字符串反序列化为java对象
     * @param json json字符串
     * @param typeReference 目标类型
     * @return 目标对象
     */
    public static <T> T jsonToBean(String json, TypeReference<T> typeReference) {
        return jsonToBean(json, JsonConfig.global(), typeReference);
    }

    /**
     * json字符串反序列化为java对象
     * @param json json字符串
     * @param config 凭证
     * @param typeReference 目标类型
     * @return 目标对象
     */
    public static <T> T jsonToBean(String json, JsonConfig config, TypeReference<T> typeReference) {
        return JsonObjectToBean.jsonToBean(json, config, typeReference);
    }

    /**
     * java对象转换为 json字符串
     * @param object java对象
     * @return json字符串
     */
    public static String beanToJson(Object object) {
        return beanToJson(object, JsonConfig.global());
    }
    /**
     * java对象转换为 json字符串
     * @param object java对象
     * @param config 配置
     * @return json字符串
     */
    public static String beanToJson(Object object, JsonConfig config) {
        try {
            return new JsonWrite(config, object).toJson();
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
    /**
     * 解析json字符串为 JSON对象
     * @param json 字符串
     * @return JSON对象：JSONObject、JsonArray
     */
    public static Object jsonToObject(String json) {
        try (JsonTokener tokener = new JsonTokener(json)){
            return tokener.parse();
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }
    /**
     * 解析json字符串为 JSON对象
     * @param json 字符串
     * @return JSON对象：JSONObject
     */
    public static JsonObject jsonToJsonObject(String json) {
        return (JsonObject)jsonToObject(json);
    }
    /**
     * 解析json字符串为 JSON对象
     * @param json 字符串
     * @return JSON对象：JsonArray
     */
    public static JsonArray jsonToJsonArray(String json) {
        return (JsonArray)jsonToObject(json);
    }


    /**
     * 解析Java对象为 JSON对象
     * @param bean Java对象
     * @return JSON对象：JSONObject、JsonArray
     */
    public static Object besnToJsonObject(Object bean) {
        if (bean == null) {
            return null;
        }
        if (bean instanceof JsonObject) {
            return bean;
        }
        if (bean instanceof JsonArray) {
            return bean;
        }
        if (bean instanceof String) {
            return bean;
        }
        return BeanToJsonObject.toJsonObject(bean);
    }
    /**
     * 解析Java对象为 JSON对象
     * @param bean Java对象
     * @return JSON对象：JSONObject
     */
    public static JsonObject jsonToJsonObject(Object bean) {
        return (JsonObject) BeanToJsonObject.toJsonObject(bean);
    }
    /**
     * 解析Java对象为 JSON对象
     * @param bean Java对象
     * @return JSON对象：JsonArray
     */
    public static JsonArray jsonToJsonArray(Object bean) {
        return (JsonArray) BeanToJsonObject.toJsonObject(bean);
    }
}

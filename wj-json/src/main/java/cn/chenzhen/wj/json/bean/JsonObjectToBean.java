package cn.chenzhen.wj.json.bean;

import cn.chenzhen.wj.json.JsonArray;
import cn.chenzhen.wj.json.JsonConfig;
import cn.chenzhen.wj.json.JsonObject;
import cn.chenzhen.wj.json.JsonUtil;
import cn.chenzhen.wj.json.bean.annotation.processor.AnnotationProcessor;
import cn.chenzhen.wj.json.bean.annotation.processor.AnnotationProcessorFactory;
import cn.chenzhen.wj.reflect.ClassUtil;
import cn.chenzhen.wj.reflect.GenericType;
import cn.chenzhen.wj.reflect.TypeReference;
import cn.chenzhen.wj.type.BeanClassTypeFactory;
import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeUtil;
import cn.chenzhen.wj.type.convert.service.date.ConvertService;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * json对象转换为 java对象
 */
public class JsonObjectToBean {
    /**
     * json对象转换为目标类型
     * @param json json对象
     * @param clazz 目标类型
     * @param config 配置
     * @return 目标对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(Object json, Class<T> clazz, JsonConfig config) {
        return (T)jsonToBean(json, new GenericType(clazz), config);
    }

    /**
     * json对象转换为目标类型
     * @param json json对象
     * @param config 配置
     * @param reference 目标类型
     * @return 目标对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(Object json, JsonConfig config, TypeReference<T> reference) {
        GenericType type = reference.getGenericType();
        return (T)jsonToBean(json, type, config);
    }

    /**
     * json对象转换为目标类型
     * @param json json对象
     * @param type 目标类型
     * @param config 配置
     * @return 目标对象
     */
    private static Object jsonToBean(Object json, GenericType type, JsonConfig config) {
        if (json == null) {
            return null;
        }
        if (json instanceof String) {
            json = JsonUtil.jsonToObject((String) json);
        }
        Class<?> cls = type.getType();
        if (cls == Object.class) {
            return json;
        }
        if (cls.isArray()) {
            return jsonArrayToArray((JsonArray) json, type, config);
        } else if (List.class.isAssignableFrom(cls) || Set.class.isAssignableFrom(cls)) {
            return jsonArrayToCollection((JsonArray) json, type, config);
        } else if (Map.class.isAssignableFrom(cls)) {
            return jsonObjectToMap((JsonObject) json, type, config);
        }
        return jsonObjectToBean((JsonObject)json, type, config);
    }

    /**
     * json对象转换为map集合
     * @param jsonObject json对象
     * @param type 目标类型
     * @param config 配置
     * @return 集合
     */
    @SuppressWarnings("unchecked")
    private static Object jsonObjectToMap(JsonObject jsonObject, GenericType type, JsonConfig config){
        if (jsonObject == null) {
            return null;
        }
        Class<?> cls = type.getType();
        TypeVariable<? extends Class<?>>[] variables = cls.getTypeParameters();
        String keyTypeName = variables[0].getName();
        String valueTypeName = variables[1].getName();
        // map key 类型
        Class<?> keyType = type.getGenericType().get(keyTypeName).getType();
        // map value 类型
        GenericType genericType = type.getGenericType().get(valueTypeName);
        if (genericType == null || genericType.getType() == Object.class) {
            return jsonObject.getData();
        }
        Map<Object, Object> map= (Map<Object, Object>)BeanClassTypeFactory.createBean(cls);
        Set<Map.Entry<String, Object>> entrySet = jsonObject.getData().entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            Object key = TypeUtil.deserializer(entry.getKey(), keyType);
            Object val = valueConvert(entry.getValue(), genericType, config);
            map.put(key, val);
        }
        return map;
    }

    /**
     * json 数组转换为集合
     * @param jsonArray json数组
     * @param type 目标类型
     * @param config 配置
     * @return 集合
     */
    @SuppressWarnings("unchecked")
    private static Object jsonArrayToCollection(JsonArray jsonArray, GenericType type, JsonConfig config){
        if (jsonArray == null) {
            return null;
        }
        Class<?> cls = type.getType();
        String typeName = cls.getTypeParameters()[0].getTypeName();
        GenericType genericType = type.getGenericType().get(typeName);
        List<Object> arrayList = jsonArray.getList();
        // 泛型丢失
        if (genericType == null || genericType.getType() == Object.class) {
            return arrayList;
        }
        Collection<Object> list= (Collection<Object>)BeanClassTypeFactory.createBean(cls);
        for (Object item : arrayList) {
            Object val = valueConvert(item, genericType, config);
            list.add(val);
        }
        return list;
    }

        /**
         * json数组转换为java数组
         * @param jsonArray json数组
         * @param type 目标类型
         * @param config 配置文件
         * @return java数组
         */
    private static Object jsonArrayToArray(JsonArray jsonArray, GenericType type, JsonConfig config){
        if (jsonArray == null) {
            return null;
        }

        Class<?> cls = type.getType();
        if (cls.isArray()) {
            // 数组类型获取实际类型
            cls = cls.getComponentType();
            type = new GenericType(cls);
        }
        if (cls == Object.class) {
            return jsonArray;
        }
        List<Object> list = jsonArray.getList();
        int size = list.size();
        Object array = Array.newInstance(cls, size);
        for (int i = 0; i < size; i++) {
            Object item = list.get(i);
            item = valueConvert(item, type, config);
            Array.set(array, i, item);
        }
        return array;

    }

    /**
     * json对象转换为java对象
     * @param json json对象
     * @param type 目标类型
     * @param config 配置文件
     * @return java对象
     */
    private static Object jsonObjectToBean(JsonObject json, GenericType type, JsonConfig config) {
        if (json == null) {
            return null;
        }
        Class<?> cls = type.getType();
        Object bean = BeanClassTypeFactory.createBean(cls);
        List<Method> methods = ClassUtil.setterMethods(cls);
        for (Method method : methods) {
            List<Annotation> ann = ClassUtil.getFieldAndMethodAnnotations(method);
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

            JsonField field = new JsonField(name, targetType.getType(), json);
            annotationProcessor(field, ann, config);
            if (field.isIgnore()) {
                continue;
            }
            if (field.isFlag()) {
                Object value = field.getValue();
                // 解包反序列化特殊处理 不在外围判断提高效率
                if (field.isUnwrappedFlag()) {
                    value = valueConvert(json, targetType, config);
                }
                ClassUtil.invoke(bean, method, value);
                continue;
            }
            Object val = json.get(field.getName());
            if (pt instanceof GenericArrayType) {
                // 泛型数组情况
                val = jsonArrayToArray((JsonArray) val, targetType, config);
            } else {
                val = valueConvert(val, targetType, config);
            }
            ClassUtil.invoke(bean, method, val);
        }
        return bean;
    }

    /**
     * 转换json值为java对象值
     * @param value 值
     * @param targetType 目标类型
     * @param config 配置文件
     * @return java对象
     */
    private static Object valueConvert(Object value, GenericType targetType, JsonConfig config){
        if (value == null) {
            return null;
        }
        Class<?> type = targetType.getType();
        // 尝试基本类型转换
        Object desVal = TypeUtil.deserializer(value, type);
        if (desVal != null) {
            return desVal;
        }
        ConvertService<?> service = DateUtil.getConvertService(type);
        if (service != null) {
            // 日期转换
            String pattern = config.getPattern(type);
            return service.deserializer(value, pattern);
        }
        // 其他对象
        return jsonToBean(value, targetType, config);
    }

    /**
     * 注解信息处理
     * @param field json字段
     * @param list 注解列表
     * @param config 配置信息
     */
    private static void annotationProcessor(JsonField field, List<Annotation> list, JsonConfig config){
        for (Annotation a : list) {
            AnnotationProcessor<Annotation> processor = AnnotationProcessorFactory.getAnnotationProcessor(a);
            if (processor == null) {
                continue;
            }
            processor.deserializer(a, field, config);
        }
    }

}

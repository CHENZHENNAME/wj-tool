package cn.chenzhen.wj.db.proxy;


import cn.chenzhen.wj.db.annotation.*;
import cn.chenzhen.wj.db.core.DbException;
import cn.chenzhen.wj.reflect.ClassUtil;
import cn.chenzhen.wj.type.TypeUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationSqlHandler {
    private static final Map<String,SqlSource> CACHE = new ConcurrentHashMap<>();
    /**
     * 解析方法上的SQL
     * @param method 方法
     * @param args 请求参数
     * @return SQL
     */
    public static BoundSql methodParse(Method method, Object[] args){
        String key = sourceId(method);
        SqlSource source = CACHE.get(key);
        if (source == null) {
            source = buildSource(method);
            CACHE.put(key, source);
        }
        Map<String, Object> map = paramParse(method, args);
        return source.getSqlEntity(map);
    }

    /**
     * 方法注解转换为 SQLSource对象
     * @param method 方法
     * @return SQL对象
     */
    private static SqlSource buildSource(Method method){
        String sql = null;
        boolean selectFlag = false;
        Select select = method.getAnnotation(Select.class);
        if (select != null) {
            sql = select.value();
            selectFlag = true;
        }
        Insert insert = method.getAnnotation(Insert.class);
        if (insert != null) {
            sql = insert.value();
        }
        Update update = method.getAnnotation(Update.class);
        if (update != null) {
            sql = update.value();
        }
        Delete delete = method.getAnnotation(Delete.class);
        if (delete != null) {
            sql = delete.value();
        }
        if (sql == null || sql.isEmpty()) {
            throw new DbException("sql not exist " + method.getName());
        }
        return new SqlSource(sql, selectFlag);
    }
    /**
     * 将参数转换为map结合
     * @param method 方法
     * @param args 请求参数
     * @return map集合
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> paramParse(Method method, Object[] args){
        Map<String, Object> map = new HashMap<>();
        if (args == null || args.length == 0) {
            return map;
        }
        int length = args.length;
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < length; i++) {
            Parameter parameter = parameters[i];
            Param param = parameter.getAnnotation(Param.class);
            map.put("param"+ i + 1, args[i]);
            if (param != null) {
                map.put(param.value(), args[i]);
            }
        }
        if (length > 1) {
            return map;
        }
        Object data = args[0];
        if (data == null) {
            return map;
        }
        // 单个入参切为自定义对象
        Class<?> dataType = data.getClass();
        if (TypeUtil.isPrimitive(dataType) || TypeUtil.isArrayOrCollection(data)) {
            // 基本类型、集合不做处理
            return map;
        }
        if (Map.class.isAssignableFrom(dataType)) {
            map.putAll((Map<String, Object>) data);
            return map;
        }
        // 自定义对象
        List<Method> methods = ClassUtil.getterMethods(dataType);
        for (Method dataMethod : methods) {
            Object val = ClassUtil.invoke(data, dataMethod);
            String fieldName = ClassUtil.getFieldName(dataMethod);
            map.put(fieldName, val);
        }
        return map;
    }

    /**
     * SQL缓存 KEY
     * @param method 请求参数
     * @return key
     */
    private static String sourceId(Method method){
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

}

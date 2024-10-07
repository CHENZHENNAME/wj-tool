package cn.chenzhen.wj.db;

import cn.chenzhen.wj.db.annotation.TableField;
import cn.chenzhen.wj.reflect.ClassUtil;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class ResultSetUtil {
    /**
     * 将结果集转换为 list集合
     * @param rs 结果集
     * @return list集合
     */
    public static List<Map<String, Object>> resultSetToList(ResultSet rs) {
        try {
            List<Map<String, Object>> list = new LinkedList<>();
            while (rs.next()) {
                Map<String, Object> result = resultSetToMap(rs);
                list.add(result);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e);
        }

    }

    /**
     * 将结果集转换为 map对象
     * @param rs 结果集
     * @param consumer 回调 消费者
     */
    public static void resultSetToMapCursor(ResultSet rs, Consumer<Map<String, Object>> consumer) {
        try {
            while (rs.next()) {
                Map<String, Object> result = resultSetToMap(rs);
                consumer.accept(result);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    /**
     * 解析一行记录为 map对象
     * @param rs 结果集
     * @return map对象
     */
    public static Map<String, Object> resultSetToMapData(ResultSet rs) {
        try {
            if (rs.next()) {
                return resultSetToMap(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e);
        }

    }

    /**
     * 解析一行记录为 map对象
     * @param rs 结果集
     * @return map对象
     */
    public static Map<String, Object> resultSetToMap(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            Map<String, Object> result = new HashMap<>();
            for (int i = 0; i < count; i++) {
                String columnName = metaData.getColumnName(i + 1);
                result.put(columnName, rs.getObject(columnName));
            }
            return result;
        } catch (SQLException e) {
            throw new DbException(e);
        }

    }

    /**
     * 将结果集转换为 对象
     * @param rs 结果集
     * @param clazz 目标对象
     * @return 对象
     * @param <T> 类型
     */
    public static  <T> T resultSetToObject(ResultSet rs, Class<T> clazz) {
        try {
            if (clazz == null) {
                return null;
            }
            if (rs.next()) {
                return resultSetToTargetObject(rs, clazz);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
        return null;
    }
    /**
     * 将结果集转换为 集合
     * @param rs 结果集
     * @param clazz 目标对象
     * @return 集合对象
     * @param <T> 类型
     */
    public static  <T> List<T> resultSetToList(ResultSet rs, Class<T> clazz) {
        try {
            if (clazz == null) {
                return null;
            }
            List<T> list = new LinkedList<>();
            while (rs.next()) {
                T result = resultSetToTargetObject(rs, clazz);
                list.add(result);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }
    /**
     * 将结果集转换为 目标对象
     * @param rs 结果集
     * @param consumer 回调 消费者
     */
    public static <T> void resultSetToObjectCursor(ResultSet rs, Consumer<T> consumer, Class<T> clazz) {
        try {
            while (rs.next()) {
                T result = resultSetToTargetObject(rs, clazz);
                consumer.accept(result);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    /**
     * 将当前结果集转换为 目标对象
     * @param rs 结果集
     * @param clazz 目标类型
     * @return 目标对象
     * @param <T> 类型
     */
    private static <T> T resultSetToTargetObject(ResultSet rs, Class<T> clazz){
        try {
            T bean = ClassUtil.newConstructor(clazz);
            List<Method> list = ClassUtil.setterMethods(clazz);
            for (Method method : list) {
                TableField ann = ClassUtil.getFieldOrMethodAnnotation(method, TableField.class);
                String name = ClassUtil.getFieldName(method);
                if (ann != null) {
                    if (ann.ignore()) {
                        continue;
                    }
                    if (!ann.value().isEmpty()) {
                        name = ann.value();
                    }
                }
                Class<?> type = method.getParameterTypes()[0];
                Object result = rs.getObject(name, type);
                ClassUtil.invoke(bean, method, result);
            }
            return bean;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

}

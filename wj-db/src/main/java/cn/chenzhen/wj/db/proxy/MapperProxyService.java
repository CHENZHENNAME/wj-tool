package cn.chenzhen.wj.db.proxy;

import cn.chenzhen.wj.db.DbUtil;
import cn.chenzhen.wj.reflect.ClassUtil;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

public class MapperProxyService implements InvocationHandler {
    private Class<?> mapperInterface;
    public <T> MapperProxyService(Class<T> type) {
        mapperInterface = type;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            // object类相关的方法
            return ClassUtil.invoke(this, method, args);
        }
        return execute(method, args);
    }
    private Object execute(Method method, Object[] args) throws Throwable {
        BoundSql boundSql = AnnotationSqlHandler.methodParse(method, args);
        String sql = boundSql.getSql();
        Object[] array = boundSql.getVariablesValue().toArray();
        if (boundSql.isSelectFlag()) {
            Type type = method.getGenericReturnType();
            // 集合、数组 、单个对 三种情况
            if (type instanceof ParameterizedType) {
                // 集合
                Type argument = ((ParameterizedType) type).getActualTypeArguments()[0];
                Class<?> typeClass = ClassUtil.getTypeClass(argument);
                return DbUtil.executeQuery(sql, typeClass, array);
            }
            Class<?> returnType = method.getReturnType();
            if (returnType.isArray()) {
                // 数组
                Class<?> componentType = returnType.getComponentType();
                List<?> list = DbUtil.executeQuery(sql, componentType, array);
                // 强转 让编译器不警告
                Object[] result = (Object[]) Array.newInstance(componentType, list.size());
                System.arraycopy(list.toArray(), 0, result, 0, list.size());
                return result;
            }
            // 单个自定义对象
            return DbUtil.executeQueryOne(sql, returnType, array);
        } else {
            return DbUtil.executeUpdate(sql, array);
        }
    }
}

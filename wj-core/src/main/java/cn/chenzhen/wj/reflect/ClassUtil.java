package cn.chenzhen.wj.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 类相关操作 工具类
 */
public class ClassUtil {

    public static<T> TypeReference<T> typeReference(Type type) {
        return new TypeReference<T>(type){};
    }

    /**
     * 尝试通过泛型名称获取实际类型
     * @param type 泛型信息
     * @param name 泛型名称
     * @return 泛型类型
     */
    public static Type getVariableType(ParameterizedType type, String name) {
        Class<?> parentType = (Class<?>)type.getRawType();
        Type[] arguments = type.getActualTypeArguments();
        TypeVariable<? extends Class<?>>[] variables = parentType.getTypeParameters();
        for (int i = 0; i < variables.length; i++) {
            if (variables[i].getName().equals(name)) {
                return arguments[i];
            }
        }
        return null;

    }

    /**
     * 获取类型 如果是泛型向上获取
     * @param type 类型
     * @return 实际类型
     */
    public static Class<?> getTypeClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        if(type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<?>) parameterizedType.getRawType();
        }
        if (type instanceof TypeVariable) {
            // TypeVariable 泛型丢失
            Type defaultType = ((TypeVariable<?>) type).getBounds()[0];
            return (Class<?>) defaultType;
        }
        if (type instanceof WildcardType) {
            // WildcardType 问号泛型
            Type defaultType = ((WildcardType) type).getLowerBounds()[0];
            return (Class<?>) defaultType;
        }
        if (type instanceof GenericArrayType) {
            // GenericArrayType 泛型数组
            Type defaultType = ((GenericArrayType) type).getGenericComponentType();
            return (Class<?>) defaultType;
        }
        return null;
    }

    /**
     * 获取方法上的注解 如果没有尝试获取字段上的注解
     * @param method 方法
     * @return 注解对象
     */
    public static <T extends Annotation> T getFieldOrMethodAnnotation(Method method, Class<T> type) {
        T ann = method.getAnnotation(type);
        if (ann != null) {
            return ann;
        }
        Field field = getMethodField(method);
        if (field == null) {
            return null;
        }
        return field.getAnnotation(type);
    }

    /**
     * 获取方法和字段上的所有注解
     * @param method 方法
     * @return 注解对象
     */
    public static List<Annotation> getFieldAndMethodAnnotations(Method method) {
        Annotation[] ann = method.getAnnotations();
        List<Annotation> list = new LinkedList<>(Arrays.asList(ann));
        Field field = getMethodField(method);
        if (field == null) {
            return list;
        }
        Annotation[] ann2 = field.getAnnotations();
        list.addAll(Arrays.asList(ann2));
        return list;
    }

    /**
     * 尝试通过方法获取对应的字段 如果不存在返回null
     * @param method 方法
     * @return 字段对象
     */
    public static Field getMethodField(Method method) {
        String name = getFieldName(method);
        try {
            return method.getDeclaringClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
    /**
     * 转换方法名称为字段名称
     * @param method 方法
     * @return 字段名称
     */
    public static String getFieldName(Method method) {
        // PropertyDescriptor desc = new PropertyDescriptor(getFiledName(method.getName()), method, null);
        String name = method.getName();
        return getFieldName(name);
    }

    /**
     * 方法名称转换为字段名称
     * @param methodName 方法名称
     * @return 字段名称
     */
    public static String getFieldName(String methodName) {
        if (methodName.startsWith("get") || methodName.startsWith("set")) {
            if (methodName.length() == 3) {
                return methodName;
            }

            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        } else if (methodName.startsWith("is")) {
            if (methodName.length() == 2) {
                return methodName;
            }
            return methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
        }
        return methodName;
    }

    /**
     * 获取所有 get 和 is开头的无参方法 无序
     * @param clazz 类
     * @return getter方法列表
     */
    public static List<Method> getterMethods(Class<?> clazz) {
        List<Method> list = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            String name = method.getName();
            if (name.startsWith("get") || name.startsWith("is")) {
                int modifiers = method.getModifiers();
                if (
                        Modifier.isStatic(modifiers)         // 静态方法
                                || method.getParameterCount() > 0   // 参数不为1
                                || "getClass".equals(name)
                                || name.length() == 3               // 方法名称长度
                ) {
                    continue;
                }

                list.add(method);
            }
        }
        return list;
    }

    /**
     * 获取所有 set 开头参数为1位的方法 无序
     * @param clazz 类
     * @return setter方法列表
     */
    public static List<Method> setterMethods(Class<?> clazz) {
        List<Method> list = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            String name = method.getName();
            if (name.startsWith("set")) {
                int modifiers = method.getModifiers();
                if (Modifier.isStatic(modifiers)             // 静态方法
                        || method.getParameterCount() != 1   // 参数不为1
                        || name.length() == 3                // 方法名称长度
                ) {
                    continue;
                }
                list.add(method);
            }
        }
        return list;
    }

    /**
     * 获取方法读一下
     * @param clazz 类
     * @param name 方法名称
     * @param parameterTypes 参数
     * @return 方法
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * 通过字段获取对应的方法
     * @param clazz 类
     * @param field 字段
     * @return 方法
     */
    public static Method getGetterMethod(Class<?> clazz, Field field) {
        String name = field.getName();
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        Method method = getMethod(clazz, "get" + name, field.getType());
        if (method != null) {
            return method;
        }
        return getMethod(clazz, "is" + name, field.getType());
    }
    /**
     * 通过字段获取对应的方法
     * @param clazz 类
     * @param field 字段
     * @return 方法
     */
    public static Method getSetterMethod(Class<?> clazz, Field field) {
        String name = field.getName();
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        return getMethod(clazz, "set" + name, field.getType());
    }

    /**
     * 获取字段值 优先尝试使用 getter方法如果没有在调用字段方法
     * @param bean 对象
     * @param field 字段
     * @return 字段值
     */
    public static Object getFieldValue(Object bean, Field field){
        try {
            Method method = getGetterMethod(bean.getClass(), field);
            if (method != null) {
                return invoke(bean, method);
            }
            field.setAccessible(true);
            return field.get(bean);
        } catch (IllegalAccessException e) {
            throw new ClassException(e);
        }
    }

    /**
     * 设置字段值 优先尝试使用 setter方法如果没有在调用字段方法
     * @param bean 对象
     * @param field 字段
     * @param value 值
     */
    public static void setFieldValue(Object bean, Field field, Object value){
        try {
            Method method = getSetterMethod(bean.getClass(), field);
            if (method != null) {
                invoke(bean, method, value);
                return;
            }
            field.setAccessible(true);
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new ClassException(e);
        }
    }

    /**
     * 通过字段名称获取字段
     * @param clazz 类
     * @param name 字段名称
     * @return 字段
     */
    public static Field getField(Class<?> clazz, String name){
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * 获取所有非静态字段 包括父类的
     * @param clazz 类
     * @return 字段
     */
    public static List<Field> getFieldList(Class<?> clazz) {
        List<Field> list = new LinkedList<>();
        Class<?> currClass = clazz;
        while (currClass != Object.class && currClass != null) {
            List<Field> fields = new LinkedList<>();
            Field[] declaredFields = currClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                fields.add(field);
            }
            // 添加到最前面
            list.addAll(0, fields);
            currClass = currClass.getSuperclass();
        }
        return list;
    }

    /**
     * 执行方法
     * @param bean 对象
     * @param method 方法
     * @param params 参数
     * @return 返回结果
     */
    public static Object invoke(Object bean, Method method, Object...params){
        try {
            method.setAccessible(true);
            return method.invoke(bean, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ClassException(e);
        }
    }

    /**
     * 创建对象
     * @param cls 类型
     * @return 实例对象
     */
    public static <T> T newConstructor(Class<T> cls){
        try {
            // Constructor<?> constructor = cls.getConstructor();
            // 如果时内部类 可以获取私有方法
            Constructor<T> constructor = cls.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            try {
                return cls.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new ClassException(ex);
            }
        }
    }

}

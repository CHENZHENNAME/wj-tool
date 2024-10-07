package cn.chenzhen.wj.reflect;

import java.lang.reflect.*;

/**
 * 泛型引用
 * @param <T> 泛型
 */
public abstract class TypeReference<T> implements Comparable<T> {
    private Type type;
    private Type[] actualTypeArguments;
    private int actualTypeArgumentIndex = 0;

    public TypeReference() {
        Type superclass = getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    public TypeReference(Type...actualTypeArguments) {
        this();
        this.actualTypeArguments = actualTypeArguments;
    }
    public GenericType getGenericType() {
        return typeToGenericType(type);
    }

    /**
     * 解析泛型信息
     * @param type 泛型信息
     * @return 泛型数
     */
    private GenericType typeToGenericType(Type type) {
        // Class 类
        if (type instanceof Class) {
            Class<?> cls = (Class<?>) type;
            GenericType genericType = new GenericType(cls);
            // 类上的泛型信息
            TypeVariable<? extends Class<?>>[] variables = cls.getTypeParameters();
            for (TypeVariable<? extends Class<?>> v : variables) {
                Type bound = v.getBounds()[0];
                bound = actualTypeArgument(bound);
                GenericType gt = typeToGenericType(bound);
                genericType.put(v.getName(), gt);
            }
            return genericType;
            // ParameterizedType 嵌套泛型
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] arguments = parameterizedType.getActualTypeArguments();
            Class<?> cls = (Class<?>) ((ParameterizedType) type).getRawType();
            TypeVariable<? extends Class<?>>[] variables = cls.getTypeParameters();
            GenericType genericType = new GenericType(cls);
            for (int i = 0; i < arguments.length; i++) {
                Type argument = arguments[i];
                String name = variables[i].getName();
                GenericType gt = typeToGenericType(argument);
                genericType.put(name, gt);
            }
            return genericType;
            // GenericArrayType 泛型数组
        } else if (type instanceof GenericArrayType) {
            GenericArrayType arrayType = (GenericArrayType) type;
            Type componentType = arrayType.getGenericComponentType();
            return typeToGenericType(componentType);
            // TypeVariable 泛型丢失
        } else if (type instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            type = typeVariable.getBounds()[0];
            type = actualTypeArgument(type);
            return typeToGenericType(type);
            // WildcardType 问号泛型
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            type = wildcardType.getUpperBounds()[0];
            type = actualTypeArgument(type);
            return typeToGenericType(type);
        }
        return null;
    }
    private Type actualTypeArgument(Type def){
        if (actualTypeArgumentIndex < actualTypeArguments.length) {
            return actualTypeArguments[actualTypeArgumentIndex++];
        }
        return def;
    }


    @Override
    public int compareTo(T o) {
        return 0;
    }
}

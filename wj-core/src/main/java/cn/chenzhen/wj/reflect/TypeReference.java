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
        type = parseType(type);
    }

    public Type getType() {
        return type;
    }
    /**
     * 解析泛型信息
     * @param type 泛型信息
     * @return 泛型数
     */
    private Type parseType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type[] arguments = pt.getActualTypeArguments();
            for (int i = 0; i < arguments.length; i++) {
                Type argument = arguments[i];
                arguments[i] = parseType(argument);
            }
            return new ParameterizedTypeImpl(arguments, pt.getRawType(), pt.getOwnerType());
        } else if (type instanceof TypeVariable) {
            // TypeVariable 泛型丢失
            Type defaultType = ((TypeVariable<?>) type).getBounds()[0];
            return actualTypeArgument(defaultType);
        } else if (type instanceof WildcardType) {
            // WildcardType 问号泛型
            Type defaultType = ((WildcardType) type).getLowerBounds()[0];
            return actualTypeArgument(defaultType);
        } else if (type instanceof GenericArrayType) {
            // GenericArrayType 泛型数组
            Type defaultType = ((GenericArrayType) type).getGenericComponentType();
            return actualTypeArgument(defaultType);
        }  else {
            return type;
        }
    }

    /**
     * 获取一个泛型缺省类型 如果已经使用完了 返回默认类型
     * @param defaultType 默认类型
     * @return 类型
     */
    private Type actualTypeArgument(Type defaultType){
        if (actualTypeArgumentIndex < actualTypeArguments.length) {
            return actualTypeArguments[actualTypeArgumentIndex++];
        }
        return defaultType;
    }


    @Override
    public int compareTo(T o) {
        return 0;
    }
}

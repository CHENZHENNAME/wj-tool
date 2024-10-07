package cn.chenzhen.wj.type.convert.service;

/**
 * 类型转换器
 * @param <T> 目标类型
 */
public interface ConvertService<T> {
    /**
     * 目标类型
     * @return 类型
     */
    Class<T> type();

    /**
     * 把值转换为目标类型
     * @param value 值
     * @return 转换后对象
     */
    T deserializer(Object value);

    /**
     * 目标对象转换为指定 基本类型
     * @param value 对象
     * @return 基本类型
     */
    default Object serialize(T value) {
        return value;
    }
}

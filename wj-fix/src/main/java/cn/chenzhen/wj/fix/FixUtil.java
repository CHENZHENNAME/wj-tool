package cn.chenzhen.wj.fix;


import cn.chenzhen.wj.reflect.TypeReference;

public class FixUtil {
    /**
     * java对象转换为 定长数据
     * @param bean 对象
     * @return 转换后的数据
     */
    public static byte[] beanToFix(Object bean) {
        return beanToFix(bean, FixConfig.global());
    }

    /**
     * java对象转换为定长数据
     * @param bean 对象
     * @param config 配置
     * @return 转换后的数据
     */
    public static byte[] beanToFix(Object bean, FixConfig config) {
        return new BeanToFix(config).toFix(bean);
    }

    /**
     * 数据转换为 目标对象
     * @param fix 数据
     * @param clazz 目标类型
     * @return 目标对象
     * @param <T> 类型
     */
    public static <T> T fixToBean(byte[] fix, Class<T> clazz) {
        return fixToBean(fix, clazz, FixConfig.global());
    }

    /**
     * 数据转换为 目标对象
     * @param fix 数据
     * @param clazz 目标类型
     * @param config 配置
     * @return 目标对象
     * @param <T> 类型
     */
    public static <T> T fixToBean(byte[] fix, Class<T> clazz, FixConfig config) {
        return new FixToBean(config).fixToBean(fix, clazz);
    }
    /**
     * 数据转换为 目标对象
     * @param fix 数据
     * @param reference 目标类型
     * @return 目标对象
     * @param <T> 类型
     */
    public static <T> T fixToBean(byte[] fix, TypeReference<T> reference) {
        return fixToBean(fix, reference, FixConfig.global());
    }
    /**
     * 数据转换为 目标对象
     * @param fix 数据
     * @param reference 目标类型
     * @param config 配置
     * @return 目标对象
     * @param <T> 类型
     */
    public static <T> T fixToBean(byte[] fix,  TypeReference<T> reference, FixConfig config) {
        return new FixToBean(config).fixToBean(fix, reference);
    }

}

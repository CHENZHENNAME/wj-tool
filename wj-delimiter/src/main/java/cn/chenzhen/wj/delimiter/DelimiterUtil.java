package cn.chenzhen.wj.delimiter;

import cn.chenzhen.wj.reflect.TypeReference;

/**
 * 分隔符字符串与对象转换工具类
 */
public class DelimiterUtil {
    /**
     * 把对象转换为 分隔符字符串
     * @param bean 对象
     * @return 分隔符字符串
     */
    public static String beanToText(Object bean) {
        return new BeanToText(DelimiterConfig.global()).toText(bean);
    }
    /**
     * 把对象转换为 分隔符字符串
     * @param bean 对象
     * @param config 配置
     * @return 分隔符字符串
     */
    public static String beanToText(Object bean, DelimiterConfig config) {
        return new BeanToText(config).toText(bean);
    }

    /**
     * 把分隔符字符串转换为 目标对象
     * @param text 分隔符字符串
     * @param type 目标类型
     * @return 目标对象
     * @param <T> 类型
     */
    public static <T> T textToBean(String text, Class<T> type) {
        return new TextToBean(DelimiterConfig.global()).toBean(text, type);
    }

    /**
     * 把分隔符字符串转换为 目标对象
     * @param text 分隔符字符串
     * @param type 目标类型
     * @param config 配置
     * @return 目标对象
     * @param <T> 类型
     */
    public static <T> T beanToText(String text, Class<T> type, DelimiterConfig config) {
        return new TextToBean(config).toBean(text, type);
    }

    /**
     * 把分隔符字符串转换为 目标对象
     * @param text 分隔符字符串
     * @param type 目标类型
     * @return 目标对象
     * @param <T> 类型
     */
    public static <T> T textToBean(String text, TypeReference<T> type) {
        return new TextToBean(DelimiterConfig.global()).toBean(text, type);
    }
    /**
     * 把分隔符字符串转换为 目标对象
     * @param text 分隔符字符串
     * @param type 目标类型
     * @param config 配置
     * @return 目标对象
     * @param <T> 类型
     */
    public static <T> T beanToText(String text, TypeReference<T> type, DelimiterConfig config) {
        return new TextToBean(config).toBean(text, type);
    }
}

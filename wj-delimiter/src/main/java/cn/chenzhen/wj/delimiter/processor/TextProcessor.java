package cn.chenzhen.wj.delimiter.processor;

import cn.chenzhen.wj.delimiter.DelimiterConfig;

import java.util.List;

public interface TextProcessor {
    /**
     * 序列化时 对单个值进行处理和转换
     * @param config 配置
     * @param text 要序列化的值
     * @return 处理后的值
     */
    String serialize(DelimiterConfig config, String text);

    /**
     * 反序列化时 传入字符串进行拆分
     * @param config 配置
     * @param text 需要反序列化的字符串
     * @return 使用分隔符拆分后的字符串
     */
    List<String> deserializer(DelimiterConfig config, String text);
}

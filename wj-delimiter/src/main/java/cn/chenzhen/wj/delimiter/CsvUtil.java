package cn.chenzhen.wj.delimiter;

import cn.chenzhen.wj.delimiter.processor.CsvTextProcessor;
import cn.chenzhen.wj.reflect.TypeReference;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * CSV 与对象转换 工具
 */
public class CsvUtil {
    private static DelimiterConfig config = new DelimiterConfig();
    static {
        config.setDelimiter(",");
        config.setEscape("\"");
        config.setTextProcessor(new CsvTextProcessor());
    }

    /**
     * 将对象转换为 csv 文本 单行
     * @param bean 对象
     * @return csv文本
     */
    public static String beanToCsv(Object bean) {
        return new BeanToText(config).toText(bean);
    }

    /**
     * 将对象转换为 csv 文本 多行
     * @param list 对象列表
     * @return csv文本
     */
    public static String beanToCsv(List<Object> list) {
        StringBuilder text = new StringBuilder();
        for (Object item : list) {
            text.append(beanToCsv(item)).append("\r\n");
        }
        return text.toString();
    }


    /**
     * cvs文本转换为 目标对象
     * @param text 单行csv文本
     * @param type 目标类型
     * @return 目标对象
     * @param <T> 目标类型
     */
    public static <T> T csvToBean(String text, Class<T> type) {
        return new TextToBean(config).toBean(text, type);
    }


    /**
     * cvs文本转换为 目标对象
     * @param text 单行csv文本
     * @param type 目标类型
     * @return 目标对象
     * @param <T> 目标类型
     */
    public static <T> T csvToBean(String text, TypeReference<T> type) {
        return new TextToBean(config).toBean(text, type);
    }




    /**
     * cvs文本转换为 目标对象
     * @param text 单行csv文本
     * @param type 目标类型
     * @return 目标对象
     * @param <T> 目标类型
     */
    public static <T> List<T> csvToList(String text, Class<T> type) {
        List<T> list = new LinkedList<>();
        List<String> csvList = csvToList(text);
        for (String row : csvList) {
            T bean = new TextToBean(config).toBean(row, type);
            list.add(bean);
        }
        return list;
    }


    /**
     * cvs文本转换为 目标对象
     * @param text 单行csv文本
     * @param type 目标类型
     * @return 目标对象
     * @param <T> 目标类型
     */
    public static <T> List<T> csvToList(String text, TypeReference<T> type) {
        List<T> list = new LinkedList<>();
        List<String> csvList = csvToList(text);
        for (String row : csvList) {
            T bean = new TextToBean(config).toBean(row, type);
            list.add(bean);
        }
        return list;
    }



    /**
     * 把csv转换为 集合 一行一条记录
     * @param text csv文本
     * @return 文本集合
     */
    public static List<String> csvToList(String text) {
        int length = text.length();
        boolean quoteFlag = false;
        int start = 0;
        List<String> list = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            // "处理
            if (text.charAt(i) == '"') {
                quoteFlag = !quoteFlag;
            } else if (text.charAt(i) == '\n') {
                // “ 里面包含换行的情况
                if (!quoteFlag) {
                    list.add(text.substring(start, i));
                    start = i + 1;
                }
            }
        }
        if (start < length) {
            list.add(text.substring(start, length));
        }
        return list;
    }

}

package cn.chenzhen.wj.delimiter.processor;

import cn.chenzhen.wj.delimiter.DelimiterConfig;

import java.util.LinkedList;
import java.util.List;

/**
 * cvs 序列化和解析
 */
public class CsvTextProcessor implements TextProcessor{
    private static final char DELIMITER = ',';
    private static final char QUOTE = '"';
    @Override
    public String serialize(DelimiterConfig config, String text) {

        if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
            return QUOTE + text.replaceAll("\"", "\"\"") + QUOTE;
        }
        return text;
    }


    @Override
    public List<String> deserializer(DelimiterConfig config, String text) {
        List<String> list = new LinkedList<>();
        int length = text.length();
        int start = 0;
        for (int i = 0; i < length; i++) {
            char code = text.charAt(i);
            if (code == QUOTE) {
                StringBuilder item = new StringBuilder();
                for (int j = i + 1; j < length; j++) {
                    char quoteCode = text.charAt(j);
                    // 双引号开始 双引号结束,
                    if (quoteCode == QUOTE) {
                        int nextIndex = j + 1;
                        // 两个双引号 转换为一个双引号
                        if (nextIndex < length && text.charAt(nextIndex) == QUOTE) {
                            j += 1;
                        } else {
                            list.add(item.toString());
                            i = nextIndex;
                            start = nextIndex;
                            break;

                        }
                    }
                    item.append(quoteCode);
                }

            }  else if (code == DELIMITER) {
                // 值,值 的情况
                String item = text.substring(start, i);
                list.add(item);
                start = i + 1;
            }
        }
        if (start < length) {
            String item = text.substring(start, length);
            list.add(item);
        }
        return list;
    }
}

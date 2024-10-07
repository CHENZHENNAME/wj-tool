package cn.chenzhen.wj.delimiter.processor;

import cn.chenzhen.wj.delimiter.DelimiterConfig;

import java.util.LinkedList;
import java.util.List;

public class DefaultTextProcessor implements TextProcessor{
    @Override
    public String serialize(DelimiterConfig config, String text) {
        String escape = config.getEscape();
        String tag = config.getDelimiter();
        if (text == null) {
            return "";
        }
        if (text.contains(tag)) {
            // String.replaceAll 使用了正则表达式 如果分隔符使用了特殊字符会出现问题
            // return text.replaceAll(tag, escape);
            return replaceAll(text, tag, escape);
        }
        return text;
    }

    private String replaceAll(String text, String tag, String escape){
        int length = text.length();
        StringBuilder newText = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (isTag(text, tag, i)) {
                newText.append(escape);
                i += tag.length() - 1;
            } else {
                newText.append(text.charAt(i));
            }
        }
        return newText.toString();
    }

    @Override
    public List<String> deserializer(DelimiterConfig config, String text) {
        List<String> result = new LinkedList<>();
        if (text == null) {
            return result;
        }
        String tag = config.getDelimiter();
        String escape = config.getEscape();
        // 可能情况：escape + tag + escape 或者 escape + tag 或者 tag + escape 或者 escape
        // 转义字符里面是否包含了 分隔符字符
        boolean tageFlag = escape.contains(tag);
        int length = text.length();
        int start = 0;
        for (int i = 0; i < length; i++) {
            if (tageFlag && isEscape(text, escape, i)) {
                i += escape.length() - 1;
                continue;
            }
            if (isTag(text, tag, i)) {
                String item = text.substring(start, i);
                // item = item.replaceAll(escape, tag);
                item= replaceAll(item, escape, tag);
                result.add(item);
                start = i + tag.length();
            }
        }
        if (start < length) {
            String item = text.substring(start, length);
            item= replaceAll(item, escape, tag);
            result.add(item);
        }
        return result;
    }
    private boolean isEscape(String text, String escape, int index){
        if (text.length() - index < escape.length()) {
            return false;
        }
        for (int i = 0; i < escape.length(); i++) {
            if (text.charAt(index + i) != escape.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean isTag(String text, String tag, int index) {
        if (text.length() - index < tag.length()) {
            return false;
        }
        for (int i = 0; i < tag.length(); i++) {
            if (text.charAt(index + i) != tag.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}

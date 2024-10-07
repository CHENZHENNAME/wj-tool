package cn.chenzhen.wj.db.util;

import java.util.Collection;

public class StrUtil {
    /**
     *
     * 字符串连接
     * @param join 连接符合
     * @param value 字符串列表
     * @return 字符串
     */
    public static String join(String join, String...value){
        int length = value.length;
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                text.append(join);
            }
            text.append(value[i]);
        }
        return text.toString();
    }


    public static String join(Collection<String> data, String val, String joinTag){
        return join(data, val, joinTag, null, null);
    }
    public static String join(Collection<String> data, String val, String joinTag, String prefix, String suffix){
        StringBuilder str = new StringBuilder();
        if (prefix != null) {
            str.append(prefix);
        }
        boolean flag = false;
        for (String s : data) {
            if (flag) {
                str.append(joinTag);
            }else {
                flag = true;
            }
            str.append(s).append(val);
        }
        if (suffix != null) {
            str.append(suffix);
        }
        return str.toString();
    }

    /**
     * 列表拼接为字符串
     * @param data 列表
     * @param joinTag 连接符号
     * @return 拼接字符串
     */
    public static String join(Collection<String> data, String joinTag){
        StringBuilder str = new StringBuilder();
        boolean flag = false;
        for (String s : data) {
            if (flag) {
                str.append(joinTag);
            }else {
                flag = true;
            }
            str.append(s);
        }
        return str.toString();
    }

    /**
     * 字符串非空判断
     * @param data 字符串
     * @param errMsg 为空时提示的错误信息
     */
    public static void notEmpty(String data, String errMsg) {
        if (data == null || data.trim().isEmpty()) {
            throw new ParamException(errMsg);
        }
    }
    /**
     * 集合非空判断
     * @param data 字符串
     * @param errMsg 为空时提示的错误信息
     */
    public static void notEmpty(Collection<?> data, String errMsg) {
        if (data == null || data.isEmpty()) {
            throw new ParamException(errMsg);
        }
    }

    public static String concat(String...data) {
        StringBuilder val = new StringBuilder();
        for (String s : data) {
            val.append(s);
        }
        return val.toString();
    }
}
class ParamException extends RuntimeException{
    public ParamException(String message) {
        super(message);
    }
}

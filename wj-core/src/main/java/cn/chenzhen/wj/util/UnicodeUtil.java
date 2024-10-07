package cn.chenzhen.wj.util;

public class UnicodeUtil {
    /**
     * 白名单 Unicode 开始
     */
    private static final int startCode = 32;
    /**
     * 白名单 Unicode 结束
     */
    private static final int endCode = 0x7f;

    /**
     * 将字符串转换为 Unicode字符串 16进制
     * @param data 字符串
     * @return 转义后的字符串
     */
    public static String escapeHex(String data){
        char[] chars = data.toCharArray();
        StringBuilder value = new StringBuilder();
        for (char item : chars) {
            if (checkCode(item)) {
                value.append(item);
            } else {
                value.append("&#x");
                value.append(charToHex(item));
                value.append(";");
            }
        }
        return value.toString();
    }


    /**
     * 将字符串转换为 Unicode字符串 10进制
     * @param data 字符串
     * @return 转义后的字符串
     */
    public static String escape(String data){
        char[] chars = data.toCharArray();
        StringBuilder value = new StringBuilder();
        for (char item : chars) {
            if (checkCode(item)) {
                value.append(item);
            } else {
                value.append("&#");
                value.append((int) item);
                value.append(";");
            }
        }
        return value.toString();
    }
    public static boolean checkCode(char code){
        return startCode <= code && code <= endCode;
    }
    private static String charToHex(char code){
        return Integer.toHexString(code);
    }
}

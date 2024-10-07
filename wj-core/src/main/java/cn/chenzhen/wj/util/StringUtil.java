package cn.chenzhen.wj.util;

import java.util.Arrays;

public class StringUtil {
    private static final byte PAD_CODE = ' ';

    /**
     * 数组填充
     * @param src 源数组
     * @param len 目标长度
     * @return 填充后的数据
     */
    public static byte[] padRight(byte[] src, int len) {
        return padRight(src, len, PAD_CODE);
    }

    /**
     * 数组填充
     * @param src 源数组
     * @param len 目标长度
     * @param pad 填充内容
     * @return 填充后的数据
     */
    public static byte[] padRight(byte[] src, int len, byte pad) {
        byte[] tmp = new byte[len];
        if (src == null) {
            Arrays.fill(tmp, pad);
            return tmp;
        }
        // int padLend = len - src.length;
        System.arraycopy(src, 0, tmp, 0, src.length);
        Arrays.fill(tmp, src.length, len, pad);
        return tmp;
    }

    /**
     * 数组填充
     * @param src 源数组
     * @param len 目标长度
     * @return 填充后的数据
     */
    public static byte[] padLeft(byte[] src, int len) {
        return padLeft(src, len, PAD_CODE);
    }
    /**
     * 数组填充
     * @param src 源数组
     * @param len 目标长度
     * @param pad 填充内容
     * @return 填充后的数据
     */
    public static byte[] padLeft(byte[] src, int len, byte pad) {
        byte[] tmp = new byte[len];
        if (src == null) {
            Arrays.fill(tmp, pad);
            return tmp;
        }
        int padLend = len - src.length;
        System.arraycopy(src, 0, tmp, padLend, src.length);
        Arrays.fill(tmp, 0, len, pad);
        return tmp;
    }
}

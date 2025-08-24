package cn.chenzhen.wj.util;

import java.util.Random;

/**
 * 汉字随机生成器
 */
public class ChineseGenerator {
    /**
     * 定义汉字Unicode区间
     * 0x4E00-0x9FA5 基本汉字
     * 0x9FA6-0x9FFF 基本汉字补充
     * 0x3400-0x4DB5 扩展A 共收录了 6582 个字符。这些字符主要是一些不常用的汉字，以及部分古代汉语、方言中的用字等。
     * 0x20000-0x2A6D6 扩展B  包含 42711 个新的汉字。这些汉字来源广泛，包括 CNS 11643 的第 4 平面到第 15 平面所收录的部分汉字、《汉语大字典》《康熙字典》中未被之前字符集收录的汉字、北朝鲜国家标准所收录的部分汉字、越南国家标准所收录的部分字喃等。
     * 0x2A700-0x2B734 扩展C 收录了约 4149 个字符，同样以汉字为主，主要是一些生僻字和罕用字，进一步补充了中日韩统一表意文字的字符集。
     * 0x2B740-0x2B81D 扩展D 中日韩越等国共用的表意文字 字符来源广泛，包括古代文献、方言用字、人名地名用字以及专业领域的特殊用字等
     * 0x2B820-0x2CEA1 扩展E 这些汉字主要来自中国大陆、日本、澳门、台湾、美国、越南等地，来源包括《辞海》《康熙字典》《喃字词典》等多种辞书以及地名、人名、姓氏用字等
     * 0x2CEB0-0x2EBE0 扩展F
     * 0x30000-0x3134A 扩展G
     */
    private static final int[][] UNICODE_RANGES = {
            {0x4E00, 0x9FA5},   // 基本汉字
            {0x9FA6, 0x9FFF},   // 基本汉字补充
            {0x3400, 0x4DB5},   // 扩展A 共收录了 6582 个字符。这些字符主要是一些不常用的汉字，以及部分古代汉语、方言中的用字等。
            {0x23404, 0x29FFF}, // 扩展B 移除 13309+1751个特色符号字符 0x20000 + 33FD = 233FD && 0x2A6D6-0x6d7= 29FFF；包含 42711 个新的汉字。这些汉字来源广泛，包括 CNS 11643 的第 4 平面到第 15 平面所收录的部分汉字、《汉语大字典》《康熙字典》中未被之前字符集收录的汉字、北朝鲜国家标准所收录的部分汉字、越南国家标准所收录的部分字喃等。
            {0x23400, 0x23402}, // 扩展B 恢复被剔除部分
    };


    /**
     * 随机获取一个字符
     * @return 字符
     */
    public static char generateChar() {
        Random random = new Random();
        int[] range = UNICODE_RANGES[random.nextInt(UNICODE_RANGES.length)];
        int start = range[0];
        int bound = range[1] + 1 - start;
        return (char) (start + random.nextInt(bound));
    }

    /**
     * 生成指定长度的随机汉字字符串
     * @param length 字符串长度
     * @return 随机汉字字符串
     */
    public static String generateString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("长度必须为正数");
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int[] range = UNICODE_RANGES[random.nextInt(UNICODE_RANGES.length)];
            int start = range[0];
            int bound = range[1] + 1 - start;
            int code = start + random.nextInt(bound);
            sb.append((char) code);
        }
        return sb.toString();
    }
}

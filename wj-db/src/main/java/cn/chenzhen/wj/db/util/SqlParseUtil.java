package cn.chenzhen.wj.db.util;

import cn.chenzhen.wj.db.proxy.BoundSql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParseUtil {
    /**
     * 正则表达式 解析 #{}
     */
    private static final Pattern HASH_PLACEHOLDER_PATTERN = Pattern.compile("#\\{\\s*([a-zA-Z0-9_$]+)\\s*\\}");
    /**
     * 正则表达式 解析 ${}
     */
    private static final Pattern DOLLAR_PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{\\s*([a-zA-Z0-9_$]+)\\s*\\}");
    /**
     * 解析 SQL 的 #{}、${} 变量
     * @param sql SQL
     * @param map 参数
     * @return 解析后SQL
     */
    public static BoundSql parse(String sql, Map<String, Object> map){
        String newSql = parseDollarSql(sql, map);
        return parseHashSql(newSql, map);
    }
    /**
     * 解析 SQL 的 ${} 变量
     * @param sql SQL
     * @param map 参数
     * @return 解析后SQL
     */
    private static String parseDollarSql(String sql, Map<String, Object> map) {
        Matcher matcher = DOLLAR_PLACEHOLDER_PATTERN.matcher(sql);
        StringBuffer builder = new StringBuffer();
        while (matcher.find()) {
            String variable = matcher.group(1);
            Object value = map.get(variable);
            matcher.appendReplacement(builder, String.valueOf(value));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    /**
     * 解析 SQL 的 #{} 变量
     * @param sql SQL
     * @param map 参数
     * @return 解析后SQL
     */
    private static BoundSql parseHashSql(String sql, Map<String, Object> map) {
        Matcher matcher = HASH_PLACEHOLDER_PATTERN.matcher(sql);
        StringBuffer builder = new StringBuffer();
        List<String> paramNames = new ArrayList<>();
        List<Object> paramValue = new ArrayList<>();
        while (matcher.find()) {
            String variable = matcher.group(1);
            paramNames.add(variable);
            Object value = map.get(variable);
            paramValue.add(value);
            matcher.appendReplacement(builder, "?");
        }
        matcher.appendTail(builder);
        return new BoundSql(builder.toString(), paramNames, paramValue);
    }
}

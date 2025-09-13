package cn.chenzhen.wj.db.core.wrapper;


import cn.chenzhen.wj.db.util.StrUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractWrapper<T extends AbstractWrapper<T>> {

    protected static final String SQL_TAG_SELECT = "SELECT";
    protected static final String SQL_TAG_UPDATE = "UPDATE";
    protected static final String SQL_TAG_DELETE = "DELETE";
    protected static final String SQL_TAG_FROM = "FROM";
    protected static final String SQL_TAG_WHERE = "WHERE";

    protected static final String SQL_TAG_SET = "SET";
    protected static final String SQL_TAG_EMPTY = " ";
    protected static final  String SQL_TAG_COMMA = ",";
    protected static final String SQL_TAG_AND = "AND";
    protected static final String SQL_TAG_OR = "OR";
    private static final String SQL_TAG_NULL = "IS NULL";

    private static final String SQL_TAG_NOT_NULL = "IS NOT NULL";
    private static final String SQL_TAG_IN = "IN";
    private static final String SQL_TAG_NOT_IN = "NOT IN";
    private static final String SQL_TAG_BETWEEN = "BETWEEN ? AND ?";
    private static final String SQL_TAG_NOT_BETWEEN = "NOT BETWEEN ? AND ?";
    private static final String SQL_TAG_LIKE = "LIKE ?";
    private static final String SQL_TAG_NOT_LIKE = "NOT LIKE ?";
    protected static final String SQL_TAG_BRACKET_LEFT = "(";
    protected static final String SQL_TAG_BRACKET_RIGHT = ")";
    protected static final String SQL_TAG_PARAM = "?";
    protected static final String SQL_TAG_PERCENT = "%";
    /**
     * 等于
     */
    protected static final String SQL_TAG_EQ = "= ?";
    /**
     * 不等于
     */
    private static final String SQL_TAG_NE = "!= ?";
    /**
     * 小于
     */
    private static final String SQL_TAG_LT = "< ?";
    /**
     * 小于等于
     */
    private static final String SQL_TAG_LE = "<= ?";
    /**
     * 大于
     */
    private static final String SQL_TAG_GT = "> ?";
    /**
     * 大于等于
     */
    private static final String SQL_TAG_GE = ">= ?";

    @SuppressWarnings("unchecked")
    private final T currType = (T) this;

    /**
     * 表名称
     */
    private final List<String> tables = new LinkedList<>();


    /**
     * 条件字段
     */
    private final List<String> whereList = new LinkedList<>();
    /**
     * 条件值
     */
    private final List<Object> whereParams = new LinkedList<>();
    /**
     *
     * 连接符标志
     */
    private boolean whereFlag = false;


    /**
     * 条件 为空
     * @param key 字段
     * @return SQL构建器
     */
    public T isNull(String key){
        return where(key, SQL_TAG_NULL);
    }
    /**
     * 条件 不为空
     * @param key 字段
     * @return SQL构建器
     */
    public T isNotNull(String key){
        return where(key, SQL_TAG_NOT_NULL);
    }


    /**
     * 条件 等于 如果value为空忽略条件
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T eq(String key, Object value){
        return where(key, SQL_TAG_EQ, value);
    }

    /**
     * 条件 不等于 如果value为空忽略条件
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T ne(String key, Object value){
        return where(key, SQL_TAG_NE, value);
    }

    /**
     * 条件 小于 如果value为空忽略条件
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T lt(String key, Object value){
        return where(key, SQL_TAG_LT, value);
    }

    /**
     * 条件 小于等于 如果value为空忽略条件
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T le(String key, Object value){
        return where(key, SQL_TAG_LE, value);
    }

    /**
     * 条件 大于 如果value为空忽略条件
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T gt(String key, Object value){
        return where(key, SQL_TAG_GT, value);
    }

    /**
     * 条件 大于等于 如果value为空忽略条件
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T ge(String key, Object value){
        return where(key, SQL_TAG_GE, value);
    }

    /**
     * 条件 包含 如果value为空忽略条件
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T in(String key, Object...value){
        return in(SQL_TAG_IN, key, value);
    }
    /**
     * 条件 包含 如果value为空忽略条件
     * @param key 字段
     * @param sql 参数化sql语句
     * @param params 参数化sql参数
     * @return SQL构建器
     */
    public T inSql(String key, String sql, Object...params){
        // xxx in (xxxx)
        String tag = SQL_TAG_IN + SQL_TAG_BRACKET_LEFT + SQL_TAG_EMPTY + sql + SQL_TAG_EMPTY + SQL_TAG_BRACKET_RIGHT;
        return where(key, tag, params);
    }
    /**
     * 条件 包含 如果value为空忽略条件
     * @param key 字段
     * @param consumer 条件
     * @return SQL构建器
     */
    public T in(String key, Consumer<T> consumer){
        T t = newWrapper();
        consumer.accept(t);
        String sql = t.getSql();
        Object[] params = t.getParams();
        return inSql(key, sql,params);
    }

    /**
     * 条件 不包含 如果value为空忽略条件
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T notIn(String key, Object...value){
        return in(SQL_TAG_NOT_IN, key, value);
    }
    /**
     * 条件 不包含 如果value为空忽略条件
     * @param key 字段
     * @param sql 参数化sql语句
     * @param params 参数化sql参数
     * @return SQL构建器
     */
    public T notInSql(String key, String sql, Object...params){
        String tag = SQL_TAG_NOT_IN + SQL_TAG_BRACKET_LEFT + SQL_TAG_EMPTY + sql + SQL_TAG_EMPTY + SQL_TAG_BRACKET_RIGHT;
        return where(key, tag, params);
    }
    /**
     * 条件 包含 如果value为空忽略条件
     * @param key 字段
     * @param consumer 条件
     * @return SQL构建器
     */
    public T notIn(String key, Consumer<T> consumer){
        T t = newWrapper();
        consumer.accept(t);
        String sql = t.getSql();
        Object[] params = t.getParams();
        return notInSql(key, sql,params);
    }

    private T in(String tag, String key, Object...value){
        StringBuilder whe = new StringBuilder(tag);
        whe.append( SQL_TAG_EMPTY);
        whe.append(SQL_TAG_BRACKET_LEFT);
        int length = value.length;
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                whe.append(SQL_TAG_COMMA).append(SQL_TAG_EMPTY);
            }
            whe.append(SQL_TAG_PARAM);
        }
        whe.append(SQL_TAG_BRACKET_RIGHT);
        tag = whe.toString();
        return where(key, tag, value);
    }


    /**
     * 范围查询
     * @param key 字段
     * @param start 开始
     * @param end 结束
     * @return SQL构建器
     */
    public T between(String key, Object start, Object end){
        return where(key, SQL_TAG_BETWEEN, start, end);
    }

    /**
     * 范围查询
     * @param key 字段
     * @param start 开始
     * @param end 结束
     * @return SQL构建器
     */
    public T notBetween(String key, Object start, Object end){
        return where(key, SQL_TAG_NOT_BETWEEN, start, end);
    }

    /**
     * 模糊查询
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T like(String key, String value){
        String tag = StrUtil.concat(SQL_TAG_PERCENT, value, SQL_TAG_PERCENT);
        return where(key, SQL_TAG_LIKE, tag);
    }

    /**
     * 模糊左查询
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T likeLeft(String key, String value){
        String tag = StrUtil.concat(SQL_TAG_PERCENT, value);
        return where(key, SQL_TAG_LIKE, tag);
    }

    /**
     * 模糊右查询
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T likeRight(String key, String value){
        String tag = StrUtil.concat(value, SQL_TAG_PERCENT);
        return where(key, SQL_TAG_LIKE, tag);
    }


    /**
     * 模糊排除查询
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T notLike(String key, String value){
        String tag = StrUtil.concat(SQL_TAG_PERCENT, value, SQL_TAG_PERCENT);
        return where(key, SQL_TAG_NOT_LIKE, tag);
    }
    /**
     * 模糊排除左查询
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T notLikeLeft(String key, String value){
        String tag = StrUtil.concat(SQL_TAG_PERCENT, value);
        return where(key, SQL_TAG_NOT_LIKE, tag);
    }

    /**
     * 模糊排除右查询
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public T notLikeRight(String key, String value){
        String tag = StrUtil.concat(value, SQL_TAG_PERCENT);
        return where(key, SQL_TAG_NOT_LIKE, tag);
    }
    /**
     * 或者
     * @return SQL构建器
     */
    public T or(){
        flag(SQL_TAG_OR, true);
        return currType;
    }
    /**
     * 或者
     * @return SQL构建器
     */
    public T or(Consumer<T> consumer){
        return where(SQL_TAG_OR, consumer);
    }

    /**
     * 或者
     * @return SQL构建器
     */
    public T and(){
        flag(SQL_TAG_AND, true);
        return currType;
    }

    /**
     * 或者
     * @return SQL构建器
     */
    public T and(Consumer<T> consumer){
        return where(SQL_TAG_AND, consumer);
    }
    private T where(String tag, Consumer<T> consumer) {
        T t = newWrapper();
        consumer.accept(t);
        String sql = t.getSql();
        Object[] params = t.getParams();
        flag(tag, true);
        whereFlag = false;
        whereList.add(sql);
        whereParams.addAll(Arrays.asList(params));
        return currType;
    }

    /**
     * 添加查询条件
     * @param key 字段
     * @param tag 条件
     * @param params 参数
     * @return SQL构建器
     */
    private T where(String key, String tag, Object...params){
        String whe = StrUtil.join(SQL_TAG_EMPTY, key, tag);
        flag(SQL_TAG_AND, false);
        whereList.add(whe);
        whereParams.addAll(Arrays.asList(params));
        whereFlag = false;
        return currType;
    }
    private void flag(String tag, boolean over){
        if (whereList.isEmpty()) {
            return;
        }
        if (whereFlag) {
            if (!over) {
                return;
            }
            whereList.set(whereList.size()-1, tag);
        }
        whereList.add(tag);
        whereFlag = true;
    }
    /**
     * 最终SQL
     * @return SQL
     */
    public String getSql(){
        String prefixSql = prefixSql();
        String whe = whereSql();
        if (prefixSql == null || prefixSql.isEmpty()) {
            return SQL_TAG_BRACKET_LEFT + whe + SQL_TAG_BRACKET_RIGHT;
        }
        // 没有 where 条件
        if (SQL_TAG_EMPTY.equals(whe)) {
            return prefixSql;
        }
        String sql = prefixSql + SQL_TAG_EMPTY + SQL_TAG_WHERE + SQL_TAG_EMPTY + whe;
        return sql;
    }

    private String whereSql() {
        if (whereList.isEmpty()) {
            return SQL_TAG_EMPTY;
        }
        return StrUtil.join(whereList, SQL_TAG_EMPTY);
    }

    /**
     * 参数列表
     * @return 参数列表
     */
    public Object[] getParams(){
        return whereParams.toArray();
    }

    protected List<Object> getWhereParams(){
        return whereParams;
    }

    /**
     * 新的 SQL构建器
     * @return SQL构建器
     */
    protected abstract T newWrapper();

    /**
     * sql前置语句 如 select/update/delete
     * @return sql前置语句
     */
    protected abstract String prefixSql();

    /**
     * 参数拼接 使用空格分开
     * @param prefix 前缀
     * @param list 参数列表
     * @return 字符串
     */
    protected static String joinParams(String prefix, List<String> list){
        int size = list.size();
        StringBuilder columnsStr = new StringBuilder(prefix);
        columnsStr.append(SQL_TAG_EMPTY);
        for (int i = 0; i < size; i++) {
            if (i >0) {
                columnsStr.append(SQL_TAG_COMMA);
                columnsStr.append(SQL_TAG_EMPTY);
            }
            columnsStr.append(list.get(i));
        }
        return columnsStr.toString();
    }

}

package cn.chenzhen.wj.db.core.bean;


import cn.chenzhen.wj.db.util.StrUtil;

import java.util.LinkedList;
import java.util.List;

public class InsertBeanSql implements BeanSql {
    String SQL_TAG_INSERT_INTO = "INSERT INTO";
    String SQL_TAG_VALUES = "VALUES";

    String SQL_TAG_BRACKET_LEFT = "(";
    String SQL_TAG_BRACKET_RIGHT = ")";
    String SQL_TAG_EMPTY = " ";
    String SQL_TAG_COMMA = ", ";
    String SQL_TAG_QUESTION = "?";

    /**
     * 表名称
     */
    private final String tableName;
    /**
     * 字段名称
     */
    private final List<String> columns = new LinkedList<>();
    /**
     * 参数
     */
    private final List<Object> params = new LinkedList<>();

    public InsertBeanSql(String tableName) {
        this.tableName = tableName;
        StrUtil.notEmpty(tableName, "表名称不能为空");
    }

    /**
     * 添加入库字段
     * @param key 字段
     * @param value 值
     */
    public void append(String key, Object value){
        columns.add(key);
        params.add(value);
    }

    /**
     * 获取参数列表
     * @return 参数列表
     */
    @Override
    public Object[] getParams(){
        return params.toArray();
    }
    @Override
    public String getSql(){
        StringBuilder sql = new StringBuilder(SQL_TAG_INSERT_INTO);
        sql.append(SQL_TAG_EMPTY).append(tableName).append(SQL_TAG_BRACKET_LEFT);
        StringBuilder val = new StringBuilder(SQL_TAG_VALUES);
        val.append(SQL_TAG_BRACKET_LEFT);
        StrUtil.notEmpty(columns, "入库字段不能为空");
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sql.append(SQL_TAG_COMMA);
                val.append(SQL_TAG_COMMA);
            }
            sql.append(columns.get(i));
            val.append(SQL_TAG_QUESTION);
        }
        sql.append(SQL_TAG_BRACKET_RIGHT);
        val.append(SQL_TAG_BRACKET_RIGHT);
        sql.append(SQL_TAG_EMPTY).append(val);
        return sql.toString();
    }


}

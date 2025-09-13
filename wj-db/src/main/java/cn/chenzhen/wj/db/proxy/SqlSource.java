package cn.chenzhen.wj.db.proxy;

import cn.chenzhen.wj.db.util.SqlParseUtil;

import java.util.Map;

public class SqlSource {
    /**
     * 原始SQL
     */
    private String sourceSql;
    /**
     * 是否查询语句
     */
    private boolean selectFlag;

    public SqlSource(String sourceSql, boolean selectFlag) {
        this.sourceSql = sourceSql;
        this.selectFlag = selectFlag;
    }

    public String getSourceSql() {
        return sourceSql;
    }

    public void setSourceSql(String sourceSql) {
        this.sourceSql = sourceSql;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    /**
     * 解析SQL
     * @param params 参数
     * @return 结果
     */
    public BoundSql getSqlEntity(Map<String, Object> params) {
        BoundSql boundSql = SqlParseUtil.parse(sourceSql, params);
        boundSql.setSelectFlag(selectFlag);
        return boundSql;
    }
}

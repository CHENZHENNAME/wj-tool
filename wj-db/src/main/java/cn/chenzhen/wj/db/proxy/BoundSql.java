package cn.chenzhen.wj.db.proxy;

import java.util.List;

/**
 * 请求参数
 */
public class BoundSql {
    /**
     * 没有解析的SQL
     */
    private String sql;
    /**
     * 查询语句
     */
    private boolean selectFlag = false;
    /**
     * 占位符变量名称
     */
    private List<String> variablesName;
    /**
     * SQL参数
     */
    private List<Object> variablesValue;

    public BoundSql(String sql, List<String> variablesName, List<Object> variablesValue) {
        this.sql = sql;
        this.variablesName = variablesName;
        this.variablesValue = variablesValue;
    }


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getVariablesValue() {
        return variablesValue;
    }

    public void setVariablesValue(List<Object> variablesValue) {
        this.variablesValue = variablesValue;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    public List<String> getVariablesName() {
        return variablesName;
    }

    public void setVariablesName(List<String> variablesName) {
        this.variablesName = variablesName;
    }
}

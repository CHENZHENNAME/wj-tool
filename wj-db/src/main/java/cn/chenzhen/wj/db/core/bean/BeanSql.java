package cn.chenzhen.wj.db.core.bean;


public interface BeanSql {
    /**
     * 获取参数列表
     *
     * @return 参数列表
     */
    Object[] getParams();

    /**
     * 获取SQL语句
     *
     * @return SQL语句
     */
    String getSql();
}

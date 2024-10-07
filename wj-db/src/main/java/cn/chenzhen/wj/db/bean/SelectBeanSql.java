package cn.chenzhen.wj.db.bean;


import cn.chenzhen.wj.db.wrapper.QueryWrapper;

public class SelectBeanSql implements BeanSql {
    private final QueryWrapper wrapper;

    public SelectBeanSql(QueryWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public Object[] getParams() {
        return wrapper.getParams();
    }

    @Override
    public String getSql() {
        return wrapper.getSql();
    }
}

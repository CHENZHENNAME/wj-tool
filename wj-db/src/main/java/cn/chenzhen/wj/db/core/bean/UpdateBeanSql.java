package cn.chenzhen.wj.db.core.bean;


import cn.chenzhen.wj.db.core.wrapper.UpdateWrapper;

public class UpdateBeanSql implements BeanSql {
    private final UpdateWrapper wrapper;

    public UpdateBeanSql(UpdateWrapper wrapper) {
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

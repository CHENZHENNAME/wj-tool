package cn.chenzhen.wj.db.bean;


import cn.chenzhen.wj.db.wrapper.UpdateWrapper;

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

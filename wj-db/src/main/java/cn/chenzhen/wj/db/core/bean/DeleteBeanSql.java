package cn.chenzhen.wj.db.core.bean;


import cn.chenzhen.wj.db.core.wrapper.DeleteWrapper;

public class DeleteBeanSql implements BeanSql {
    private final DeleteWrapper wrapper;

    public DeleteBeanSql(DeleteWrapper wrapper) {
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

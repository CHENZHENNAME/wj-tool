package cn.chenzhen.wj.db.bean;


import cn.chenzhen.wj.db.wrapper.DeleteWrapper;

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

package cn.chenzhen.wj.db.wrapper;


import cn.chenzhen.wj.db.util.StrUtil;

public class DeleteWrapper extends AbstractWrapper<DeleteWrapper> {
    private String table;

    public DeleteWrapper from(String table) {
        this.table =  table;
        return this;
    }


    @Override
    protected String prefixSql() {
        StrUtil.notEmpty(table, "表名称不能为空");
        // delete from xxx
        return StrUtil.concat(SQL_TAG_DELETE, SQL_TAG_EMPTY, SQL_TAG_FROM, SQL_TAG_EMPTY, table, SQL_TAG_EMPTY);
    }

    @Override
    protected DeleteWrapper newWrapper() {
        return new DeleteWrapper();
    }
}

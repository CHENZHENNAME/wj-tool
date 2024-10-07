package cn.chenzhen.wj.db.wrapper;


import cn.chenzhen.wj.db.util.StrUtil;

import java.util.LinkedList;
import java.util.List;

public class UpdateWrapper extends AbstractWrapper<UpdateWrapper> {
    private String table;

    /**
     * 更新字段
     */
    private final List<String> updateColumns = new LinkedList<>();
    /**
     * 字段值
     */
    private final List<Object> params = new LinkedList<>();
    public UpdateWrapper from(String table) {
        this.table =  table;
        return this;
    }

    /**
     * 添加查询字段
     * @param key 字段
     * @param value 值
     * @return SQL构建器
     */
    public UpdateWrapper set(String key, Object value){
        updateColumns.add(key + SQL_TAG_EMPTY + SQL_TAG_EQ);
        params.add(value);
        return this;
    }

    @Override
    protected String prefixSql() {
        StrUtil.notEmpty(table, "表名称不能为空");
        // update xxx set
        String sql = StrUtil.concat(SQL_TAG_UPDATE, SQL_TAG_EMPTY, table, SQL_TAG_EMPTY, SQL_TAG_SET);
        // update xxx set xxx, xxx, xxx
        String update = joinParams(sql, updateColumns);
        return update;
    }

    @Override
    protected UpdateWrapper newWrapper() {
        return new UpdateWrapper();
    }

    @Override
    public Object[] getParams(){
        LinkedList<Object> list = new LinkedList<>();
        list.addAll(params);
        list.addAll(super.getWhereParams());
        return list.toArray();
    }
}

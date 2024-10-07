package cn.chenzhen.wj.db.wrapper;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QueryWrapper extends AbstractWrapper<QueryWrapper> {
    /**
     * 字段名称
     */
    private final List<String> tables = new LinkedList<>();
    private final List<String> columns = new LinkedList<>();
    /**
     * 添加查询字段
     * @param tables 表列表
     * @return SQL构建器
     */
    public QueryWrapper from(String...tables){
        this.tables.addAll(Arrays.asList(tables));
        return this;
    }


    /**
     * 添加查询字段
     * @param columns 字段
     * @return SQL构建器
     */
    public QueryWrapper select(String...columns){
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    @Override
    protected String prefixSql() {
        if (tables.isEmpty() || columns.isEmpty()) {
            return "";
        }
        // select xxx,xxx,xxx.....
        String select =  joinParams(SQL_TAG_SELECT, columns);
        // from xxx,xxx,xxx
        String from = joinParams(SQL_TAG_FROM, tables);

        return select + SQL_TAG_EMPTY + from;
    }


    @Override
    protected QueryWrapper newWrapper() {
        return new QueryWrapper();
    }
}

package cn.chenzhen.wj.json;

import java.util.LinkedList;
import java.util.List;

/**
 * JSON数组
 */
public class JsonArray {
    /**
     * JSON数据数据列表
     */
    private List<Object> list = new LinkedList<>();

    /**
     * 新增数据
     * @param data 数据
     */
    public void add(Object data){
        list.add(data);
    }

    /**
     * 获取数据
     * @param index 下标
     * @return 数据
     */
    public Object get(int index){
        return list.get(index);
    }

    /**
     * 删除数据
     * @param index 下标
     * @return 数据
     */
    public Object remove(int index){
        return list.remove(index);
    }

    /**
     * 返回所有数据
     * @return 数据列表
     */
    public List<Object> getList() {
        return list;
    }

    /**
     * 新增所有数据 会覆盖原数据
     * @param list 数据
     */
    public void setList(List<Object> list) {
        this.list = list;
    }
}

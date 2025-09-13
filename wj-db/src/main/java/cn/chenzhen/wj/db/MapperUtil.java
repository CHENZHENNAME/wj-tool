package cn.chenzhen.wj.db;

import cn.chenzhen.wj.db.core.ConnectionManager;
import cn.chenzhen.wj.db.core.DbException;
import cn.chenzhen.wj.db.core.bean.BeanSql;
import cn.chenzhen.wj.db.core.bean.SqlBeanUtil;
import cn.chenzhen.wj.db.core.wrapper.QueryWrapper;
import cn.chenzhen.wj.db.core.wrapper.UpdateWrapper;
import cn.chenzhen.wj.db.proxy.MapperProxyService;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.Consumer;

public class MapperUtil {
    /**
     * 创建 mapper接口代理对象
     * @param clazz mapper类型
     * @return 对象
     * @param <T> mapper接口类型
     */
    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> clazz){
        return (T) Proxy.newProxyInstance(MapperUtil.class.getClassLoader(), new Class[]{clazz}, new MapperProxyService(clazz));
    }
    /**
     * 保存对象到数据库 自动生成insert语句
     * 空字段不入库
     * @param bean 对象
     * @return 结果
     */
    public static int insert(Object bean){
        return insert(ConnectionManager.DEFAULT, bean);
    }
    /**
     * 保存对象到数据库 自动生成insert语句
     * 空字段不入库
     * @param type 数据库类型
     * @param bean 对象
     * @return 结果
     */
    public static int insert(String type, Object bean){
        if (bean == null) {
            throw new DbException("保存对象不能为空");
        }
        BeanSql beanSql = SqlBeanUtil.beanToInsert(bean);
        return DbUtil.executeUpdateType(type, beanSql.getSql(), beanSql.getParams());
    }

    /**
     * 更新对象到数据库 自动生成update语句
     * 空字段不入库
     * @param bean 对象
     * @return 结果
     */
    public static int update(Object bean){
        return update(ConnectionManager.DEFAULT, bean);
    }

    /**
     * 更新对象到数据库
     * @param wrapper 对象
     * @return 结果
     */
    public static int update(UpdateWrapper wrapper){
        return update(ConnectionManager.DEFAULT, wrapper);
    }

    /**
     * 更新对象到数据库 自动生成update语句
     * 空字段不入库
     * @param type 数据库类型
     * @param bean 对象
     * @return 结果
     */
    public static int update(String type, Object bean){
        if (bean == null) {
            throw new DbException("修改对象不能为空");
        }
        BeanSql beanSql = SqlBeanUtil.beanToUpdate(bean);
        return DbUtil.executeUpdateType(type, beanSql.getSql(), beanSql.getParams());
    }

    /**
     * 更新对象到数据库 自动生成update语句
     * 空字段不入库
     * @param type 数据库类型
     * @param wrapper 对象
     * @return 结果
     */
    public static int update(String type, UpdateWrapper wrapper){
        if (wrapper == null) {
            throw new DbException("修改对象不能为空");
        }
        return DbUtil.executeUpdateType(type, wrapper.getSql(), wrapper.getParams());
    }



    /**
     * 删除对象到数据库 自动生成delete语句
     * 空字段不入库
     * @param bean 对象
     * @return 结果
     */
    public static int delete(Object bean){
        return delete(ConnectionManager.DEFAULT, bean);
    }
    /**
     * 删除对象到数据库 自动生成delete语句
     * 空字段不入库
     * @param type 数据库类型
     * @param bean 对象
     * @return 结果
     */
    public static int delete(String type, Object bean){
        if (bean == null) {
            throw new DbException("删除对象不能为空");
        }
        BeanSql beanSql = SqlBeanUtil.beanToDelete(bean);
        return DbUtil.executeUpdateType(type, beanSql.getSql(), beanSql.getParams());
    }
    /**
     * 传入对象生成查询条件
     * @param bean 对象
     * @return 查询结果
     * @param <T> 返回类型
     */
    public static <T> List<T> select(T bean) {
        return select(ConnectionManager.DEFAULT, bean);
    }

    /**
     * 传入对象生成查询条件
     * @param type 数据库类型
     * @param bean 对象
     * @return 查询结果
     * @param <T> 返回类型
     */
    public static <T> List<T> select(String type, T bean) {
        BeanSql beanSql = SqlBeanUtil.beanToSelect(bean);
        @SuppressWarnings("unchecked")
        Class<T> cls = (Class<T>) bean.getClass();
        return DbUtil.executeQueryType(type, beanSql.getSql(), cls, beanSql.getParams());
    }

    /**
     * SQL构建器查询
     * @return 查询结果
     * @param <T> 返回类型
     */
    public static <T> List<T> select(QueryWrapper wrapper, Class<T> type) {
        return select(ConnectionManager.DEFAULT, wrapper, type);
    }

    /**
     * SQL构建器查询
     * @param type 数据库类型
     * @param wrapper 查询条件
     * @param clsType 返回对象类型
     * @return 返回列表
     * @param <T> 返回类型
     */
    public static <T> List<T> select(String type, QueryWrapper wrapper, Class<T> clsType) {
        return DbUtil.executeQueryType(type, wrapper.getSql(), clsType, wrapper.getParams());
    }


    /**
     * SQL构建器查询 游标模式
     * @param wrapper 查询条件
     * @param type 返回对象类型
     * @param consumer 游标回调
     * @param <T> 返回类型
     */
    public static <T> void select(QueryWrapper wrapper, Class<T> type, Consumer<T> consumer) {
        select(ConnectionManager.DEFAULT, wrapper, type, consumer);
    }

    /**
     * SQL构建器查询 游标模式
     * @param wrapper 查询条件
     * @param type 返回对象类型
     * @param consumer 游标回调
     * @param <T> 返回类型
     */
    public static <T> void select(String type, QueryWrapper wrapper, Class<T> clsType, Consumer<T> consumer) {
        DbUtil.executeQueryType(type, wrapper.getSql(), clsType, consumer, wrapper.getParams());
    }

    /**
     * 传入对象生成查询条件
     * @param bean 对象
     * @return 查询结果
     * @param <T> 返回类型
     */
    public static <T> T selectOne(T bean) {
        return selectOne(ConnectionManager.DEFAULT, bean);
    }

    /**
     * 传入对象生成查询条件
     * @param type 数据库类型
     * @param bean 对象
     * @return 查询结果
     * @param <T> 返回类型
     */
    public static <T> T selectOne(String type, T bean) {
        BeanSql beanSql = SqlBeanUtil.beanToSelect(bean);
        @SuppressWarnings("unchecked")
        Class<T> cls = (Class<T>) bean.getClass();
        return DbUtil.executeQueryOneType(type, beanSql.getSql(), cls, beanSql.getParams());
    }

    /**
     * SQL构建器查询
     * @return 查询结果
     * @param <T> 返回类型
     */
    public static <T> T selectOne(QueryWrapper wrapper, Class<T> type) {
        return selectOne(ConnectionManager.DEFAULT, wrapper, type);
    }

    /**
     * SQL构建器查询
     * @param type 数据库类型
     * @param wrapper 查询条件
     * @param clsType 返回对象类型
     * @return 查询结果
     * @param <T> 返回类型
     */
    public static <T> T selectOne(String type, QueryWrapper wrapper, Class<T> clsType) {
        return DbUtil.executeQueryOneType(type, wrapper.getSql(), clsType, wrapper.getParams());
    }
}

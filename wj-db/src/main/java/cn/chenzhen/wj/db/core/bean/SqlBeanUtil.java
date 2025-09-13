package cn.chenzhen.wj.db.core.bean;

import cn.chenzhen.wj.db.core.DbException;
import cn.chenzhen.wj.db.annotation.Table;
import cn.chenzhen.wj.db.annotation.TableField;
import cn.chenzhen.wj.db.core.wrapper.DeleteWrapper;
import cn.chenzhen.wj.db.core.wrapper.QueryWrapper;
import cn.chenzhen.wj.db.core.wrapper.UpdateWrapper;
import cn.chenzhen.wj.reflect.ClassUtil;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 对象转换为SQL工具类
 */
public class SqlBeanUtil {
    /**
     * 生成insertSQL
     * @param bean 对象
     * @return insert SQL
     */
    public static BeanSql beanToInsert(Object bean){
        Class<?> type = bean.getClass();
        String tableName = getTableName(bean);
        InsertBeanSql db = new InsertBeanSql(tableName);
        List<TableFieldMetadata> metadata = getterTableField(bean, type);
        for (TableFieldMetadata item : metadata) {
            Object value = item.getValue();
            if (value == null) {
                continue;
            }
            db.append(item.getName(), value);
        }
        return db;
    }

    /**
     * 生成更新SQL
     * @param bean 对象
     * @return update SQL
     */
    public static BeanSql beanToUpdate(Object bean){
        Class<?> type = bean.getClass();
        String tableName = getTableName(bean);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.from(tableName);
        UpdateBeanSql db = new UpdateBeanSql(wrapper);
        List<TableFieldMetadata> metadata = getterTableField(bean, type);
        boolean whereFlag = false;
        for (TableFieldMetadata item : metadata) {
            Object value = item.getValue();
            if (value == null) {
                continue;
            }
            if (item.isPrimaryKey()) {
                wrapper.eq(item.getName(), value);
                whereFlag = true;
            } else {
                wrapper.set(item.getName(), value);
            }
        }
        if (!whereFlag) {
            throw new DbException("update where not exists");
        }
        return db;
    }

    /**
     * 生成 delete SQL
     * @param bean 对象
     * @return delete SQL
     */
    public static BeanSql beanToDelete(Object bean){
        Class<?> type = bean.getClass();
        String tableName = getTableName(bean);
        DeleteWrapper wrapper = new DeleteWrapper();
        wrapper.from(tableName);
        DeleteBeanSql db = new DeleteBeanSql(wrapper);
        List<TableFieldMetadata> metadata = getterTableField(bean, type);
        boolean whereFlag = false;
        for (TableFieldMetadata item : metadata) {
            Object value = item.getValue();
            if (value == null) {
                continue;
            }
            wrapper.eq(item.getName(), value);
            whereFlag = true;
        }
        if (!whereFlag) {
            throw new DbException("delete where not exists");
        }
        return db;
    }

    /**
     * 生成查询SQL
     * @param bean 对象
     * @return 查询SQL
     */
    public static BeanSql beanToSelect(Object bean){
        Class<?> type = bean.getClass();
        String tableName = getTableName(bean);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.from(tableName);
        SelectBeanSql db = new SelectBeanSql(wrapper);
        List<TableFieldMetadata> metadata = getterTableField(bean, type);
        for (TableFieldMetadata item : metadata) {
            String name = item.getName();
            wrapper.select(name);
            Object value = item.getValue();
            if (value == null) {
                continue;
            }
            wrapper.eq(name, value);
        }
        return db;
    }

    /**
     * 将对象转换为 数据库字段
     * @param bean 对象
     * @param type 类型
     * @return 数据库字段数据
     */
    public static List<TableFieldMetadata> getterTableField(Object bean, Class<?> type){
        List<Method> list = ClassUtil.getterMethods(type);
        List<TableFieldMetadata> metadata = new LinkedList<>();
        for (Method method : list) {
            TableField ann = ClassUtil.getFieldOrMethodAnnotation(method, TableField.class);
            String name = ClassUtil.getFieldName(method);
            boolean primaryKey = false;
            if (ann != null) {
                if (ann.ignore()) {
                    continue;
                }
                if (!ann.value().isEmpty()) {
                    name = ann.value();
                }
                primaryKey = ann.primaryKey();
            }
            Object result = ClassUtil.invoke(bean, method);
            TableFieldMetadata fieldMetadata = new TableFieldMetadata(name, primaryKey, result);
            metadata.add(fieldMetadata);
        }
        return metadata;
    }

    /**
     * 获取数据库表名称
     * @param bean 对象
     * @return 数据库表名称
     */
    public static String getTableName(Object bean){
        Class<?> cls = bean.getClass();
        Table ann = cls.getAnnotation(Table.class);
        if (ann == null || ann.value().isEmpty()) {
            return cls.getSimpleName();
        }
        return ann.value();
    }
}

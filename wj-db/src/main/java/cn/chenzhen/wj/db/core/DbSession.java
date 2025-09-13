package cn.chenzhen.wj.db.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DbSession extends DbTransaction{
    private final Logger logger = LoggerFactory.getLogger(DbSession.class);
    private final Connection connection;

    public DbSession(Connection conn) {
        this.connection = conn;
    }


    /**
     * 执行SQL 增删改
     * @param sql SQL
     * @param params 参数
     * @return 影响行数
     */
    public  int executeUpdate(String sql, Object... params){
        logger.debug("sql:{} , params:{}", sql, params);
        return DbExecute.executeUpdate(connection, sql, params);
    }
    //--------------------------- 转换为 map 类型 START ---------------------------------------------------------------------------------------

    /**
     * 执行指定SQL 获取第一行数据结果
     * @param sql 参数化SQL
     * @param params 参数
     * @return 第一行结果（<列名称, 值>）
     */
    public Map<String, Object> executeQueryOne(String sql, Object... params){
        return DbExecute.executeQueryOne(connection, sql, params);
    }

    /**
     * 执行指定SQL 将结果集转换为map集合
     * @param sql 参数化SQL
     * @param params 参数
     * @return 集合（<列名称, 值>）
     */
    public List<Map<String, Object>> executeQuery(String sql, Object... params){
        return DbExecute.executeQuery(connection, sql, params);
    }


    /**
     * 执行指定SQL 将结果集转换为map集合
     * @param sql 参数化SQL
     * @param params 参数
     */
    public void executeQuery(String sql, Consumer<Map<String, Object>> consumer, Object... params){
        DbExecute.executeQuery(connection, sql, consumer, params);
    }

    //--------------------------- 转换为 map 类型 END ---------------------------------------------------------------------------------------
    //--------------------------- 转换为 对象 类型 START ---------------------------------------------------------------------------------------

    /**
     * 执行指定SQL 获取第一行数据 转换为目标类型
     * @param sql 参数化SQL
     * @param clazz 目标类型
     * @param params 参数
     * @return 目标对象
     */
    public <T> T executeQueryOne(String sql, Class<T> clazz, Object... params){
        return DbExecute.executeQueryOne(connection, sql, clazz, params);
    }

    /**
     * 执行指定SQL 将结果集转换 转换为目标类型 集合
     * @param sql 参数化SQL
     * @param clazz 目标类型
     * @param params 参数
     * @return 目标对象 集合
     */
    public <T> List<T> executeQuery(String sql, Class<T> clazz, Object... params){
        return DbExecute.executeQuery(connection, sql, clazz, params);
    }



    /**
     * 执行指定SQL 将结果集转换为map集合
     * @param sql 参数化SQL
     * @param clazz 目标类型
     * @param consumer 结果消费者 回调
     * @param params 参数
     */
    public <T> void executeQuery(String sql, Class<T> clazz, Consumer<T> consumer, Object... params){
        DbExecute.executeQuery(connection, sql, clazz, consumer, params);
    }

    //--------------------------- 转换为 对象 类型 END ---------------------------------------------------------------------------------------


}

package cn.chenzhen.wj.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DbUtil extends DbTransaction{
    private static final Logger logger = LoggerFactory.getLogger(DbUtil.class);

    /**
     * 执行SQL 增删改
     * @param sql SQL
     * @param params 参数
     * @return 影响行数
     */
    public  static int executeUpdate(String sql, Object... params){
        return executeUpdateType(ConnectionManager.DEFAULT, sql, params);
    }
    /**
     * 执行SQL 增删改
     * @param type 数据库连接类型
     * @param sql SQL
     * @param params 参数
     * @return 影响行数
     */
    public  static int executeUpdateType(String type, String sql, Object... params){
        logger.debug("type:{}, sql:{} , params:{}",type, sql, params);
        try (Connection connection = ConnectionManager.getConnection(type);){
            return DbExecute.executeUpdate(connection, sql, params);
        }catch (Exception e) {
            throw new DbException(e);
        }
    }
    //--------------------------- 转换为 map 类型 START ---------------------------------------------------------------------------------------

    /**
     * 执行指定SQL 获取第一行数据结果
      * @param sql 参数化SQL
     * @param params 参数
     * @return 第一行结果（<列名称, 值>）
     */
    public static Map<String, Object> executeQueryOne(String sql, Object... params){
        return executeQueryOneType(ConnectionManager.DEFAULT, sql, params);
    }

    /**
     * 执行指定SQL 将结果集转换为map集合
     * @param sql 参数化SQL
     * @param params 参数
     * @return 集合（<列名称, 值>）
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params){
        return executeQuery(ConnectionManager.DEFAULT, sql, params);
    }

    /**
     * 执行指定SQL 获取第一行数据结果
      * @param type 数据库连接类型
      * @param sql 参数化SQL
     * @param params 参数
     * @return 第一行结果（<列名称, 值>）
     */
    public static Map<String, Object> executeQueryOneType(String type, String sql, Object... params){
        logger.debug("type:{}, sql:{} , params:{}",type, sql, params);
        try (Connection connection = ConnectionManager.getConnection(type);){
            return DbExecute.executeQueryOne(connection, sql, params);

        }catch (Exception e) {
            throw new DbException(e);
        }
    }

    /**
     * 执行指定SQL 将结果集转换为map集合
     * @param type 数据库连接类型
     * @param sql 参数化SQL
     * @param params 参数
     * @return 集合（<列名称, 值>）
     */
    public static List<Map<String, Object>> executeQuery(String type, String sql, Object... params){
        logger.debug("type:{}, sql:{} , params:{}",type, sql, params);
        try (Connection connection = ConnectionManager.getConnection(type);){
            return DbExecute.executeQuery(connection, sql, params);

        }catch (Exception e) {
            throw new DbException(e);
        }
    }


    /**
     * 执行指定SQL 将结果集转换为map集合
     * @param sql 参数化SQL
     * @param params 参数
     */
    public static void executeQuery(String sql, Consumer<Map<String, Object>> consumer, Object... params){
        executeQuery(ConnectionManager.DEFAULT, sql, consumer, params);
    }
    /**
     * 执行指定SQL 将结果集转换为map集合
     * @param type 数据库连接类型
     * @param sql 参数化SQL
     * @param consumer 消费对象
     * @param params 参数
     */
    public static void executeQuery(String type, String sql, Consumer<Map<String, Object>> consumer, Object... params){
        logger.debug("type:{}, sql:{} , params:{}",type, sql, params);
        try (Connection connection = ConnectionManager.getConnection(type);){
            DbExecute.executeQuery(connection, sql, consumer, params);
        }catch (Exception e) {
            throw new DbException(e);
        }
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
    public static <T> T executeQueryOne(String sql, Class<T> clazz, Object... params){
        return executeQueryOne(ConnectionManager.DEFAULT, sql, clazz, params);
    }

    /**
     * 执行指定SQL 将结果集转换 转换为目标类型 集合
     * @param sql 参数化SQL
     * @param clazz 目标类型
     * @param params 参数
     * @return 目标对象 集合
     */
    public static <T> List<T> executeQuery(String sql, Class<T> clazz, Object... params){
        return executeQuery(ConnectionManager.DEFAULT, sql, clazz, params);
    }

    /**
     * 执行指定SQL 获取第一行数据 转换为目标类型
     * @param type 数据库连接类型
     * @param sql 参数化SQL
     * @param clazz 目标类型
     * @param params 参数
     * @return 目标对象
     */
    public static <T> T executeQueryOne(String type, String sql, Class<T> clazz, Object... params){
        logger.debug("type:{}, sql:{} , params:{}",type, sql, params);
        try (Connection connection = ConnectionManager.getConnection(type);){
            return DbExecute.executeQueryOne(connection, sql, clazz, params);

        }catch (Exception e) {
            throw new DbException(e);
        }
    }

    /**
     * 执行指定SQL 将结果集 转换为目标类型
     * @param type 数据库连接类型
     * @param sql 参数化SQL
     * @param clazz 目标类型
     * @param params 参数
     * @return 目标对象 集合
     */
    public static <T> List<T> executeQuery(String type, String sql, Class<T> clazz, Object... params){
        logger.debug("type:{}, sql:{} , params:{}",type, sql, params);
        try (Connection connection = ConnectionManager.getConnection(type);){
            return DbExecute.executeQuery(connection, sql, clazz, params);

        }catch (Exception e) {
            throw new DbException(e);
        }
    }


    /**
     * 执行指定SQL 将结果集转换为map集合
     * @param sql 参数化SQL
     * @param clazz 目标类型
     * @param consumer 结果消费者 回调
     * @param params 参数
     */
    public static <T> void executeQuery(String sql, Class<T> clazz, Consumer<T> consumer, Object... params){
        executeQuery(ConnectionManager.DEFAULT, sql, clazz, consumer, params);
    }
    /**
     * 执行指定SQL 将结果集转换为map集合
     * @param type 数据库连接类型
     * @param sql 参数化SQL
     * @param consumer 消费对象
     * @param params 参数
     */
    public static <T> void executeQuery(String type, String sql, Class<T> clazz, Consumer<T> consumer, Object... params){
        logger.debug("type:{}, sql:{} , params:{}",type, sql, params);
        try (Connection connection = ConnectionManager.getConnection();){
            DbExecute.executeQuery(connection, sql, clazz, consumer, params);

        }catch (Exception e) {
            throw new DbException(e);
        }
    }
    //--------------------------- 转换为 对象 类型 END ---------------------------------------------------------------------------------------

}

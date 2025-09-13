package cn.chenzhen.wj.db.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DbExecute {
    private static final Logger logger = LoggerFactory.getLogger(DbExecute.class);

    /**
     * 增删改 指定数据
     * @param sql SQL语句
     * @param params 参数
     * @return 执行结果
     */
    public static int executeUpdate(Connection connection, String sql, Object...params){
        logger.debug("SQL:{}, params:{}", sql, params);
        try (PreparedStatement ps = prepareStatement(connection, sql, params);){
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    /**
     * 查询指定数据库数据
     * @param sql SQL语句
     * @param params 参数
     * @return 查询结果
     */
    public static List<Map<String, Object>> executeQuery(Connection connection, String sql, Object...params){
        logger.debug("SQL:{}, params:{}", sql, params);
        try (
                PreparedStatement ps = prepareStatement(connection, sql, params);
                ResultSet resultSet = ps.executeQuery();
        ){
            return ResultSetUtil.resultSetToList(resultSet);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    /**
     * 查询指定数据库数据
     * @param sql SQL语句
     * @param params 参数
     * @return 查询结果
     */
    public static Map<String, Object> executeQueryOne(Connection connection, String sql, Object...params){
        logger.debug("SQL:{}, params:{}", sql, params);
        try (
                PreparedStatement ps = prepareStatement(connection, sql, params);
                ResultSet resultSet = ps.executeQuery();
        ){
            return ResultSetUtil.resultSetToMapData(resultSet);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    /**
     * 查询指定数据库数据
     * @param sql SQL语句
     * @param targetType 转换对象类型
     * @param params 参数
     * @return 查询结果
     */
    public static <T> List<T> executeQuery(Connection connection, String sql, Class<T> targetType, Object...params){
        logger.debug("SQL:{}, params:{}", sql, params);
        try (
                PreparedStatement ps = prepareStatement(connection, sql, params);
                ResultSet resultSet = ps.executeQuery();
        ){
            return ResultSetUtil.resultSetToList(resultSet, targetType);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    /**
     * 查询指定数据库数据
     * @param sql SQL语句
     * @param targetType 转换对象类型
     * @param params 参数
     * @return 查询结果
     */
    public static <T> T executeQueryOne(Connection connection, String sql, Class<T> targetType, Object...params){
        logger.debug("SQL:{}, params:{}", sql, params);
        try (
                PreparedStatement ps = prepareStatement(connection, sql, params);
                ResultSet resultSet = ps.executeQuery();
        ){
            return ResultSetUtil.resultSetToObject(resultSet, targetType);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    /**
     * 查询指定数据库数据 游标模式
     * @param sql SQL语句
     * @param consumer 回调函数
     * @param params 参数
     */
    public static <T> void executeQuery(Connection connection, String sql,  Consumer<Map<String, Object>> consumer, Object...params){
        logger.debug("SQL:{}, params:{}", sql, params);
        try (
                PreparedStatement ps = prepareStatement(connection, sql, params);
                ResultSet resultSet = ps.executeQuery();
        ){
            ResultSetUtil.resultSetToMapCursor(resultSet, consumer);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    /**
     * 查询指定数据库数据 游标模式
     * @param sql SQL语句
     * @param targetType 转换对象类型
     * @param consumer 回调函数
     * @param params 参数
     */
    public static <T> void executeQuery(Connection connection, String sql, Class<T> targetType, Consumer<T> consumer, Object...params){
        logger.debug("SQL:{}, params:{}", sql, params);
        try (
                PreparedStatement ps = prepareStatement(connection, sql, params);
                ResultSet resultSet = ps.executeQuery();
        ){
            ResultSetUtil.resultSetToObjectCursor(resultSet, consumer, targetType);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    /**
     * 创建查询器
     * @param conn 连接对象
     * @param sql SQL语句
     * @param params 参数
     * @return 查询器
     * @throws SQLException 异常
     */
    public static PreparedStatement prepareStatement(Connection conn, String sql, Object...params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        int length = params.length;
        for (int i = 0; i < length; i++) {
            ps.setObject(i+1, params[i]);
        }
        return ps;
    }
}

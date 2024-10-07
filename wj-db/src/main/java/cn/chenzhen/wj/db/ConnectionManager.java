package cn.chenzhen.wj.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public static final String DEFAULT = "DEFAULT";
    private static final Map<String, ConnectionConfig> CONNECTION_CONFIG = new ConcurrentHashMap<>();
    private static final ThreadLocal<Map<String, Connection>> CACHE = new ThreadLocal<>();

    /**
     * 注册默认连接数据库信息
     * @param url 连接地址
     * @param username 账号
     * @param password 密码
     */
    public static void registerConnectionConfig(String url, String username, String password) {
        CONNECTION_CONFIG.put(DEFAULT, new ConnectionConfig(url, username, password));
    }

    /**
     * 注册连接数据库信息
     * @param type 连接类型
     * @param url 连接地址
     * @param username 账号
     * @param password 密码
     */
    public static void registerConnectionConfig(String type, String url, String username, String password) {
        CONNECTION_CONFIG.put(type, new ConnectionConfig(url, username, password));
    }

    public static void registerConnectionConfig(DataSource dataSource) {
        CONNECTION_CONFIG.put(DEFAULT, new ConnectionConfig(dataSource));
    }
    public static void registerConnectionConfig(String type, DataSource dataSource) {
        CONNECTION_CONFIG.put(type, new ConnectionConfig(dataSource));
    }

    /**
     * 获取默认连接数据库对象
     * @return 连接对象
     */
    public static Connection getConnection() {
        return createConnection(DEFAULT);
    }

    /**
     * 获取指定连接数据库对象 如果开启事务了返回同一个连接对象
     * @return 连接对象
     */
    public static Connection getConnection(String type) {
        Connection conn = cacheConn(type);
        if (conn != null) {
            return conn;
        }
        return createConnection(type);
    }

    /**
     * 创建指定类型的数据库连接对象
     * @param type 数据类型
     * @return 连接对象
     */
    private static Connection createConnection(String type) {
        ConnectionConfig config = CONNECTION_CONFIG.get(type);
        if (config == null) {
            throw new DbException("connection type " + type + " not exist");
        }
        try {
            // 有序使用连接池
            if (config.getDataSource()!= null) {
                return config.getDataSource().getConnection();
            }
            return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
        } catch (SQLException e) {
            throw new DbException(e);
        }

    }

    /**
     * 开启事务
     * @param type 数据库连接类型
     * @return 回话对象
     */
    public static DbSession open(String type) {
        try {
            Map<String, Connection> map = CACHE.get();
            if (map == null) {
                map = new ConcurrentHashMap<>();
                CACHE.set(map);
            }
            Connection conn = map.get(type);
            if (conn == null) {
                conn = ConnectionManager.getConnection(type);
                map.put(type, conn);
            }
            conn.setAutoCommit(false);
            return new DbSession(conn);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    /**
     * 提交事务
     * @param type 数据库连接类型
     */
    public static void commit(String type) {
        try {
            Connection conn = cacheConn(type);
            if (conn == null) {
                throw new DbException("未开启事务 不需要提交事务");
            }
            conn.commit();
        } catch (SQLException e) {
            throw new DbException(e);
        }finally {
            removeConn(type);
        }
    }

    /**
     * 回滚事务
     * @param type 数据库连接类型
     */
    public static void rollback(String type) {
        try {
            Connection conn = cacheConn(type);
            if (conn == null) {
                throw new DbException("未开启事务 不需要回滚事务");
            }
            conn.rollback();
        } catch (SQLException e) {
            throw new DbException(e);
        }finally {
            removeConn(type);
        }
    }

    /**
     * 获取缓存连接对象
     * @param type 数据库连接类型
     * @return 数据库连接对象
     */
    private static Connection cacheConn(String type){
        Map<String, Connection> map = CACHE.get();
        if (map == null) {
            return null;
        }
        Connection conn = map.get(type);
        return conn;
    }
    /**
     * 删除缓存
     * @param type 数据库连接类型
     */
    private static void removeConn(String type){
        Map<String, Connection> map = CACHE.get();
        if (map == null) {
            return;
        }
        map.remove(type);
        if (map.isEmpty()) {
            CACHE.remove();
        }
    }
}

package cn.chenzhen.wj.db;

import javax.sql.DataSource;

/**
 * jdbc 数据库连接信息
 */
public class ConnectionConfig {
    /**
     * 连接地址
     */
    private String url;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 数据库连接池
     */
    private DataSource dataSource;
    /**
     * 驱动
     */
    private String driver;

    public ConnectionConfig(String url) {
        this.url = url;
    }

    public ConnectionConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public ConnectionConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}

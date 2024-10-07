package cn.chenzhen.wj.db;

import cn.chenzhen.wj.db.annotation.Table;
import cn.chenzhen.wj.db.annotation.TableField;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TestDbUtil {
    static {
        ConnectionManager.registerConnectionConfig("jdbc:h2:./test", "root", "root");
    }
    @Test
    public void test_CreateTable(){
        String sql = "CREATE TABLE IF NOT EXISTS test_db_user(\n" +
                "\tid int auto_increment primary KEY COMMENT 'ID',\n" +
                "\tname VARCHAR COMMENT '姓名',\n" +
                "\tsex VARCHAR COMMENT '性别',\n" +
                "\tstatus int COMMENT '状态 0 正常 1 禁用'\n" +
                ")";
        ConnectionManager.registerConnectionConfig("jdbc:h2:./test", "root", "root");
        int count = DbUtil.executeUpdate(sql);
        System.out.println(count);
    }
    @Test
    public void test_insert(){
        String sql = "INSERT INTO test_db_user(name,sex,status) VALUES(?,?,?)";
        int count = DbUtil.executeUpdate(sql, "测试","女","0");
        System.out.println(count);
    }

    @Test
    public void test_update(){
        String sql = "INSERT INTO test_db_user(name,sex,status) VALUES(?,?,?)";
        int count = DbUtil.executeUpdate(sql, "测试","女","0");
        sql = "update TEST_DB_USER set name = ?, sex = ?, status =?  where id = ?";
        count = DbUtil.executeUpdate(sql, "测试——update", "女", "1", "1");
        System.out.println(count);
    }


    @Test
    public void test_delete(){
        String sql = "INSERT INTO test_db_user(name,sex,status) VALUES(?,?,?)";
        int count = DbUtil.executeUpdate(sql, "测试-DELETE","女","0");
        sql = "select * from test_db_user where name = ?";
        Map<String, Object> map = DbUtil.executeQueryOne(sql, "测试-DELETE");

        sql = "delete from TEST_DB_USER  where id = ?";
        count = DbUtil.executeUpdate(sql, map.get("ID"));
        System.out.println(count);
    }

    @Test
    public void test_query1(){
        String sql = "select * from test_db_user";
        List<Map<String, Object>> list = DbUtil.executeQuery(sql);
        for (Map<String, Object> map : list) {
            System.out.println(map.get("ID"));
            System.out.println(map.get("NAME"));
            System.out.println(map.get("SEX"));
            System.out.println(map.get("STATUS"));
        }
    }
    @Test
    public void test_query2(){
        String sql = "select * from test_db_user";
        DbUtil.executeQuery(sql, new Consumer<Map<String, Object>>(){
            @Override
            public void accept(Map<String, Object> map) {
                System.out.println(map.get("ID"));
                System.out.println(map.get("NAME"));
                System.out.println(map.get("SEX"));
                System.out.println(map.get("STATUS"));
                System.out.println("-----------");
            }
        } );
    }
    @Test
    public void test_query3(){
        String sql = "select * from test_db_user";
        Map<String, Object> map = DbUtil.executeQueryOne(sql);
        System.out.println(map.get("ID"));
        System.out.println(map.get("NAME"));
        System.out.println(map.get("SEX"));
        System.out.println(map.get("STATUS"));
    }

    @Test
    public void test_query4(){
        String sql = "select * from test_db_user";
        List<TestDBUser> list = DbUtil.executeQuery(sql, TestDBUser.class);
        for (TestDBUser user : list) {
            System.out.println(user.getId());
            System.out.println(user.getName());
            System.out.println(user.getUserSex());
            System.out.println(user.getStatus());
            System.out.println("-----------");
        }
    }
    @Test
    public void test_query5(){
        String sql = "select * from test_db_user";
        TestDBUser user = DbUtil.executeQueryOne(sql, TestDBUser.class);
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getUserSex());
        System.out.println(user.getStatus());
        System.out.println("-----------");
    }

    @Test
    public void test_query6(){
        String sql = "select * from test_db_user";
        DbUtil.executeQuery(sql, TestDBUser.class, new Consumer<TestDBUser>() {
            @Override
            public void accept(TestDBUser user) {
                System.out.println(user.getId());
                System.out.println(user.getName());
                System.out.println(user.getUserSex());
                System.out.println(user.getStatus());
                System.out.println("-----------");
            }
        });
    }

}


@Table("TEST_DB_USER")
class TestDBUser {
    @TableField(value = "id", primaryKey = true)
    private Integer id;
    private String name;
    @TableField("sex")
    private String userSex;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
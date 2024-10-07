package cn.chenzhen.wj.db;

import cn.chenzhen.wj.db.wrapper.QueryWrapper;
import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class TestDatabase {
    private static final Logger logger = LoggerFactory.getLogger(TestDatabase.class);
    static {
        // ConnectionManager.registerConnectionConfig("jdbc:h2:./test", "root", "root");
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl("jdbc:h2:./test");
        ds.setUsername("root");
        ds.setPassword("root");
        ConnectionManager.registerConnectionConfig(ds);
    }
    @Test
    public void test_CreateTable(){
        String sql = "CREATE TABLE IF NOT EXISTS test_user(\n" +
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
    public void test_Insert(){
        TestUser user = new TestUser();
        // user.setId(0);
        user.setName("测试");
        user.setSex("女");
        user.setStatus(0);
        int count = MapperUtil.insert(user);
        System.out.println(count);
    }
    @Test
    public void test_Update(){
        TestUser user = new TestUser();
        // user.setId(0);
        user.setName("测试");
        TestUser result = MapperUtil.selectOne(user);
        System.out.println(result.getId());
        System.out.println(result.getName());
        System.out.println(result.getSex());
        user.setName("测试——UPDATE");
        int count = MapperUtil.update(result);
        System.out.println(count);
    }
    @Test
    public void test_Delete(){
        TestUser saveUser = new TestUser();
        saveUser.setName("测试_Delete");
        saveUser.setSex("女");
        saveUser.setStatus(0);
        int count = MapperUtil.insert(saveUser);
        System.out.println(count);
        TestUser queryUser = new TestUser();
        // user.setId(0);
        queryUser.setName(saveUser.getName());
        TestUser result = MapperUtil.selectOne(queryUser);
        System.out.println(result.getId());
        System.out.println(result.getName());
        System.out.println(result.getSex());
        count = MapperUtil.delete(result);
        System.out.println(count);
    }
    @Test
    public void test_Query(){
        TestUser saveUser = new TestUser();
        saveUser.setName("测试_Query");
        saveUser.setSex("女");
        saveUser.setStatus(0);
        int count = MapperUtil.insert(saveUser);
        System.out.println(count);
        TestUser queryUser = new TestUser();
        // user.setId(0);
        queryUser.setName(saveUser.getName());
        TestUser result = MapperUtil.selectOne(queryUser);
        System.out.println(result.getId());
        System.out.println(result.getName());
        System.out.println(result.getSex());
    }
    @Test
    public void test_QueryList(){
        QueryWrapper wrapper = new QueryWrapper()
                .select("ID","name","sex","status")
                .from("TEST_USER");

        List<TestUser> list = MapperUtil.select(wrapper, TestUser.class);
        for (TestUser user : list) {
            System.out.println(user.getId());
            System.out.println(user.getName());
            System.out.println(user.getSex());
            System.out.println("-----------");
        }
    }
    @Test
    public void test_QueryConsumer(){
        QueryWrapper wrapper = new QueryWrapper()
                .select("ID","name","sex","status")
                .from("TEST_USER")
                // .eq("1","2")
                ;

        MapperUtil.select(wrapper, TestUser.class, new Consumer<TestUser>() {
            @Override
            public void accept(TestUser user) {
                System.out.println(user.getId());
                System.out.println(user.getName());
                System.out.println(user.getSex());
                System.out.println("-----------");
            }
        });

    }
}

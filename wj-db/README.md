# 数据库操作工具
## 背景
用惯了springboot+mybatis 有一天单独写个工具需要连接数据库懵了不会了，于是乎诞生了这个数据库操作工具
## 示例demo
### 数据库连接配置
~~~java
// 普通连接
ConnectionManager.registerConnectionConfig("jdbc:h2:./test", "root", "root");

// 注册数据库连接池
DruidDataSource ds = new DruidDataSource();
ds.setUrl("jdbc:h2:./test");
ds.setUsername("root");
ds.setPassword("root");
ConnectionManager.registerConnectionConfig(ds);
~~~
### DbUtil 增删改查
~~~java
// 新增
String sql = "INSERT INTO test_db_user(name,sex,status) VALUES(?,?,?)";
int count = DbUtil.executeUpdate(sql, "测试","女","0");

// 修改
String sql = "update TEST_DB_USER set name = ?, sex = ?, status =?  where id = ?";
int count = DbUtil.executeUpdate(sql, "测试——update", "女", "1", "1");

// 删除
String sql = "delete from TEST_DB_USER  where id = ?";
int count = DbUtil.executeUpdate(sql, "1");

// 查询
// 查询单个结果
String sql = "select * from test_db_user";
Map<String, Object> map = DbUtil.executeQueryOne(sql);
// 查询所有结果
String sql = "select * from test_db_user";
List<Map<String, Object>> list = DbUtil.executeQuery(sql);

// 游标查询
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
//------------------------------------------------------
// 数据库对象
@Table("TEST_DB_USER")
class TestDBUser {
    @TableField(value = "id", primaryKey = true)
    private Integer id;
    private String name;
    @TableField("sex")
    private String userSex;
    private Integer status;
    // 省略 get/set
}

// 查询结果转换为对象
String sql = "select * from test_db_user";
List<TestDBUser> list = DbUtil.executeQuery(sql, TestDBUser.class);
for (TestDBUser user : list) {
    System.out.println(user.getId());
    System.out.println(user.getName());
    System.out.println(user.getUserSex());
    System.out.println(user.getStatus());
    System.out.println("-----------");
}
// 查询单个结果
String sql = "select * from test_db_user";
TestDBUser user = DbUtil.executeQueryOne(sql, TestDBUser.class);
System.out.println(user.getId());
System.out.println(user.getName());
System.out.println(user.getUserSex());
System.out.println(user.getStatus());

// 游标查询
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

~~~
## MapperUtil 增删改查
~~~java
// 数据库对象
@Table("TEST_USER")
class TestUser{
    @TableField(value = "id", primaryKey = true)
    private Integer id;
    private String name;
    private String sex;
    private Integer status;
    // 省略 get/set
}

// 新增
TestUser user = new TestUser();
user.setName("测试");
user.setSex("女");
user.setStatus(0);
int count = MapperUtil.insert(user);
// 修改
TestUser user = new TestUser();
user.setName("测试");
TestUser result = MapperUtil.selectOne(user);
result.setName("测试——UPDATE");
int count = MapperUtil.update(result);

// 删除
TestUser saveUser = new TestUser();
saveUser.setName("测试_Delete");
saveUser.setSex("女");
saveUser.setStatus(0);
int count = MapperUtil.insert(saveUser);
TestUser queryUser = new TestUser();
queryUser.setName(saveUser.getName());
TestUser result = MapperUtil.selectOne(queryUser);
count = MapperUtil.delete(result);
// 查询
// 查询单个结果
TestUser queryUser = new TestUser();
queryUser.setName(saveUser.getName());
TestUser result = MapperUtil.selectOne(queryUser);
System.out.println(result.getId());
System.out.println(result.getName());
System.out.println(result.getSex());

// 查询多个结果
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

// 通过游标查询
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

~~~

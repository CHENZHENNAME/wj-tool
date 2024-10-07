## 前言
父亲过世总要给他留点什么来留做纪念，但是想想自己啥也不会也只能留一点代码长期维护作为纪念。

项目名称为父亲名称简拼
## 介绍
用惯了 mybatis、stringboot、jackson、fastjson、hutool框架后有一天自己想依葫芦画瓢写一个简单版的，查看这些源码发现自己看不懂整的都怀疑自己了，但是转念一想人家多少年的努力出来的结果我要是轻轻松松就看懂了那还有啥技术，没办法只能转头看看其他简单的项目

wj-cor项目参考了fatsjson项目代码进行编写

wj-json、wj-xml本项目参考 org.json.json

## 背景
在某银行对接的第三方系统都是一些奇奇怪怪的报文，起初报文字段十几个通过groovy字符串模板用的很顺手，直到某基金，保险，银联的报文出现了几百个字段，整个人都麻了，于是乎尝试把报文转变为java对象的方案在心里诞生了。
## 模块
| 模块           | 功能             | 描述                                                       |
|--------------|----------------|----------------------------------------------------------|
| wj-core      | wj项目的核心工具类     | 类型转换、反射、注解信息获取相关操作工具类                                    |
| wj-json      | json序列化和反序列化模块 | 参考 org.json.json项目进行编写                                   |
| wj-xml       | xml序列化和反序列化    | 对xml字符串转换为java对象或者java对象转换为xml字符串操作                      |
| wj-fix       | 定长报文的拆包和拼包     | 对定长报文拆包为指定java对象 以及对指定java对象转换为定长报文                      |
| wj-delimiter | 分隔符报文的拆包和拼包    | 对分隔符报文拆包为指定java对象 以及指定java对象转换为分隔符报文、csv与java对象的序列化和反序列化 |
| wj-db        | 数据库增删改查工具类     | 对原始的jdbc进行封装值 只需要定义数据库映射对象就可以进行增删改查                      |



## 简单示例
### wj-json
~~~java
class Item{
    private String name;
    private Integer age;

    public Item() {}

    public Item(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    // 省略get/set
}
Item item = new Item("测试", 18);
// 序列化json
String json = JsonUtil.beanToJson(item);
System.out.println(json);
// 反序列化
Item jsonItem = JsonUtil.jsonToBean(json, Item.class);

~~~
### wj-xml
~~~java
class Item{
    @Xml(
            attribute = {
                    @Attribute(attrName = "attr_key1" , attrValue = "attr_v1"),
                    @Attribute(attrName = "attr_key2" , attrValue = "attr_v2")
            }
    )
    private String name;
    @Attr("Item_attr")
    private String attr;

    public Item() {
    }
    public Item(String name, String attr) {
        this.name = name;
        this.attr = attr;
    }
    // 省略get/set
}
Item item = new Item("测试才是","哈哈哈");
// 序列化 XML 结果：<Item Item_attr="哈哈哈"><name attr_key1="attr_v1" attr_key2="attr_v2">测试才是</name></Item>
String xml = XmlUtil.beanToXml(item);
System.out.println(xml);
// 反序列化 xml
Item bean = XmlUtil.xmlToBean(xml, new TypeReference<Item>() {});

~~~
### wj-fix
~~~java

class Parent {
    @Fix(10)
    private String data1;
    @Fix(20)
    private String data2;
    // 省略 get/set
}
class Item extends Parent{
    @Fix(30)
    private String name;
    @Fix(10)
    private String sex;
    // 省略 get/set
}

Item item = new Item();
item.setData1("11");
item.setData2("22");
item.setName("33");
item.setSex("44");
// 序列化 定长报文 结果：11        22                  33                            44        
byte[] bytes = FixUtil.beanToFix(item);
System.out.println(new String(bytes));
// 反序列化
Item bean = FixUtil.fixToBean(bytes, Item.class);
~~~
### wj-delimiter
~~~java
class Item{
    private String name;
    private String sex;

    public Item() {
    }

    public Item(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }
    // 省略 get/set
}
// 分隔符
Item item = new Item("测试", "哈哈");
// 序列化为 分隔符字符串： 测试|哈哈
String text = DelimiterUtil.beanToText(item);
// 反序列化
Item bean = DelimiterUtil.textToBean(text, Item.class);

// csv
Item item = new Item("测,,试", "哈,,哈");
// 序列化 ："测,,试","哈,,哈"
String text = CsvUtil.beanToCsv(item);
// 反序列化
Item bean = CsvUtil.csvToBean(text, Item.class);



~~~
### wj-db
~~~java
// 数据库映射对象
@Table("TEST_USER")
class TestUser{
    @TableField(value = "id", primaryKey = true)
    private Integer id;
    private String name;
    private String sex;
    private Integer status;
    // 省略get/set
}

// 数据连接
ConnectionManager.registerConnectionConfig("jdbc:h2:./test", "root", "root");

// 新增
TestUser user = new TestUser();
user.setName("测试");
user.setSex("女");
user.setStatus(0);
int count = MapperUtil.insert(user);

// 修改
TestUser user = new TestUser();
user.setId(0);
user.setName("测试");
user.setSex("女");
user.setStatus(0);
int count = MapperUtil.update(result);

// 删除
TestUser user = new TestUser();
user.setId(0);
MapperUtil.delete(user);

// 查询
TestUser queryUser = new TestUser();
queryUser.setName(saveUser.getName());
TestUser result = MapperUtil.selectOne(queryUser);
// 查询
QueryWrapper wrapper = new QueryWrapper().select("ID","name","sex","status").from("TEST_USER");
List<TestUser> list = MapperUtil.select(wrapper, TestUser.class);
// 查询游标
QueryWrapper wrapper = new QueryWrapper().select("ID","name","sex","status").from("TEST_USER");
MapperUtil.select(wrapper, TestUser.class, user -> {
    System.out.println(user.getId());
    System.out.println(user.getName());
    System.out.println(user.getSex());
    System.out.println("-----------");
});
~~~
package cn.chenzhen.wj;

import cn.chenzhen.wj.json.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsonObjectToBean {
    @Test
    public void test(){
        Item item = new Item("测试", 18);
        String json = JsonUtil.beanToJson(item);
        System.out.println(json);
        Item jsonItem = JsonUtil.jsonToBean(json, Item.class);
        Assertions.assertEquals(item.getName(), jsonItem.getName(), "结果错误");
        Assertions.assertEquals(item.getAge(), jsonItem.getAge(), "结果错误");
    }
    static class Item{
        private String name;
        private Integer age;

        public Item() {
        }

        public Item(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}


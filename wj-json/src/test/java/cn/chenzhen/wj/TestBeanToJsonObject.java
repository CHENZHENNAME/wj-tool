package cn.chenzhen.wj;

import cn.chenzhen.wj.json.JsonArray;
import cn.chenzhen.wj.json.JsonObject;
import cn.chenzhen.wj.json.bean.BeanToJsonObject;
import cn.chenzhen.wj.json.bean.annotation.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class TestBeanToJsonObject {
    @Test
    public void test_ObjectToJson(){
        Object json = BeanToJsonObject.toJsonObject(new Item1("测试菜市场", "val1"));
        Assertions.assertNotNull(json,"结果错误");
        Assertions.assertEquals(json.getClass(), JsonObject.class,"结果错误");
        JsonObject obj = (JsonObject) json;
        Assertions.assertEquals(obj.get("name"), "测试菜市场","结果错误");
        Assertions.assertEquals(obj.get("test"), "val1","结果错误");
    }

    @Test
    public void test_ArrayToJson(){
        Object[] array = {1, 2, "33", "44"};
        Object json = BeanToJsonObject.toJsonObject(array);
        Assertions.assertNotNull(json,"结果错误");
        Assertions.assertEquals(json.getClass(), JsonArray.class,"结果错误");
        JsonArray jsonArray = (JsonArray) json;
        Assertions.assertEquals(jsonArray.get(0), 1,"结果错误");
        Assertions.assertEquals(jsonArray.get(1), 2,"结果错误");
        Assertions.assertEquals(jsonArray.get(2), "33","结果错误");
        Assertions.assertEquals(jsonArray.get(3), "44","结果错误");
    }

    @Test
    public void test_ObjectJson(){
        Data<Data<List<Object>>> data = new Data<>("测试1", new Data<>("测试2", Arrays.asList(
                new Data<>("测试3", 1),
                new Data<>("测试4", "val1"),
                new Data<>("测试5", true),
                new Data<>("测试6", false),
                new Data<>("测试7", new BigDecimal("1.11")),
                new Data<>("测试8", new BigInteger("22")),
                new Data<>("测试9", Arrays.asList(1, 2, 3, 4)),
                Arrays.asList(1, 2, 3, 4, 5)
        )));

        Object json = BeanToJsonObject.toJsonObject(data);
        Assertions.assertNotNull(json,"结果错误");
        Assertions.assertEquals(json.getClass(), JsonObject.class,"结果错误");
        JsonObject jsonObject = (JsonObject) json;
        Assertions.assertEquals(jsonObject.get("name"), "测试1", "结果错误");
        Assertions.assertEquals(jsonObject.get("data").getClass(), JsonObject.class, "结果错误");
        Assertions.assertEquals(((JsonObject)jsonObject.get("data")).get("name"), "测试2", "结果错误");
        Assertions.assertEquals(((JsonObject)jsonObject.get("data")).get("data").getClass(), JsonArray.class, "结果错误");
        JsonArray array = (JsonArray) ((JsonObject) jsonObject.get("data")).get("data");
        {
            JsonObject item = (JsonObject)array.get(0);
            Assertions.assertEquals(item.get("name"), "测试3", "结果错误");
            Assertions.assertEquals(item.get("data"), 1, "结果错误");
        }
        {
            JsonObject item = (JsonObject)array.get(1);
            Assertions.assertEquals(item.get("name"), "测试4", "结果错误");
            Assertions.assertEquals(item.get("data"), "val1", "结果错误");
        }
        {
            JsonObject item = (JsonObject)array.get(2);
            Assertions.assertEquals(item.get("name"), "测试5", "结果错误");
            Assertions.assertEquals(item.get("data"), true, "结果错误");
        }
        {
            JsonObject item = (JsonObject)array.get(3);
            Assertions.assertEquals(item.get("name"), "测试6", "结果错误");
            Assertions.assertEquals(item.get("data"), false, "结果错误");
        }
        {
            JsonObject item = (JsonObject)array.get(4);
            Assertions.assertEquals(item.get("name"), "测试7", "结果错误");
            Assertions.assertEquals(item.get("data"), new BigDecimal("1.11"), "结果错误");
        }
        {
            JsonObject item = (JsonObject)array.get(5);
            Assertions.assertEquals(item.get("name"), "测试8", "结果错误");
            Assertions.assertEquals(item.get("data"), new BigInteger("22"), "结果错误");
        }
        {
            JsonObject item = (JsonObject)array.get(6);
            Assertions.assertEquals(item.get("name"), "测试9", "结果错误");
            JsonArray list = (JsonArray) item.get("data");
            Assertions.assertEquals(list.get(0), 1, "结果错误");
            Assertions.assertEquals(list.get(1), 2, "结果错误");
            Assertions.assertEquals(list.get(2), 3, "结果错误");
            Assertions.assertEquals(list.get(3), 4, "结果错误");
        }
        {
            JsonArray item = (JsonArray)array.get(7);
            Assertions.assertEquals(item.get(0), 1, "结果错误");
            Assertions.assertEquals(item.get(1), 2, "结果错误");
            Assertions.assertEquals(item.get(2), 3, "结果错误");
            Assertions.assertEquals(item.get(3), 4, "结果错误");
            Assertions.assertEquals(item.get(4), 5, "结果错误");
        }
    }


    static class Data<T> {
        private String name;
        private T data;
        public Data(String name, T data) {
            this.name = name;
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
    static class Item1{
        private String name;
        @Json("test")
        private String data1;

        public Item1() {
        }

        public Item1(String name, String data1) {
            this.name = name;
            this.data1 = data1;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getData1() {
            return data1;
        }

        public void setData1(String data1) {
            this.data1 = data1;
        }
    }
}

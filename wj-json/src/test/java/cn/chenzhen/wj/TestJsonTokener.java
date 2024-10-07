package cn.chenzhen.wj;

import cn.chenzhen.wj.json.JsonArray;
import cn.chenzhen.wj.json.JsonException;
import cn.chenzhen.wj.json.JsonObject;
import cn.chenzhen.wj.json.JsonTokener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class TestJsonTokener {
    @Test
    public void test_errorJson(){
        parseErrorJson("222");
        parseErrorJson("aaa");
        parseErrorJson("{");
        parseErrorJson("{\"a\"}");
        parseErrorJson("{\"a\":}");
        parseErrorJson("{\"a\":\"}");
        parseErrorJson("{\"a\":\"\"");
        parseErrorJson("[");
        parseErrorJson("[1");
        parseErrorJson("[1,");
        parseErrorJson("[1,]");
        parseErrorJson("[1,2");
        parseErrorJson("[1,2,[1]");

    }
    private void parseErrorJson(String json){
        try {
            new JsonTokener(json).parse();
            Assertions.fail("解析错误");
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), JsonException.class);
        }
    }
    @Test
    public void test_JsonEmpty(){
        Object obj = new JsonTokener("{}").parse();
        Assertions.assertNotNull(obj, "返回值为空");
        Assertions.assertEquals(obj.getClass(), JsonObject.class, "类型错误");
        JsonObject jsonObject = (JsonObject) obj;
        Map<String, Object> data = jsonObject.getData();
        Assertions.assertEquals(data.size(), 0, "解析结果错误");
    }
    @Test
    public void test_JsonArrayEmpty(){
        Object obj = new JsonTokener("[]").parse();
        Assertions.assertNotNull(obj, "返回值为空");
        Assertions.assertEquals(obj.getClass(), JsonArray.class, "类型错误");
        JsonArray array = (JsonArray) obj;
        Assertions.assertEquals(array.getList().size(), 0, "解析结果错误");
    }
    @Test
    public void test_json(){
        String json = "{\"test_key\": \"test_value\"}";
        try (ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
             JsonTokener tokener = new JsonTokener(is)){
            Object obj = tokener.parse();
            Assertions.assertNotNull(obj, "返回值为空");
            Assertions.assertEquals(obj.getClass(), JsonObject.class, "类型错误");
            JsonObject jsonObject = (JsonObject) obj;
            Assertions.assertEquals(jsonObject.get("test_key"), "test_value", "解析结果错误");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_parseSimpleJson() {
        String json = "{\"test_key\": \"test_value\"}";
        Object obj = new JsonTokener(json).parse();
        Assertions.assertNotNull(obj, "返回值为空");
        Assertions.assertEquals(obj.getClass(), JsonObject.class, "类型错误");
        JsonObject jsonObject = (JsonObject) obj;
        Assertions.assertEquals(jsonObject.get("test_key"), "test_value", "解析结果错误");
    }

    @Test
    public void test_parseSimpleJson2() {
        String json = "{\"test\": \"aa\\\"bb\"}";
        Object obj = new JsonTokener(json).parse();
        Assertions.assertNotNull(obj, "返回值为空");
        Assertions.assertEquals(obj.getClass(), JsonObject.class, "类型错误");
        JsonObject jsonObject = (JsonObject) obj;
        Assertions.assertEquals(jsonObject.get("test"), "aa\"bb", "解析结果错误");
    }

    @Test
    public void test_parseSimpleJson3() {
        String json = "{\n" +
                "  \"test1\": \"aa\\\"bb\",\n" +
                "  \"test2\": \"   aa\\\"bb   \"\n" +
                "}";
        Object obj = new JsonTokener(json).parse();
        Assertions.assertNotNull(obj, "返回值为空");
        Assertions.assertEquals(obj.getClass(), JsonObject.class, "类型错误");
        JsonObject jsonObject = (JsonObject) obj;
        Assertions.assertEquals(jsonObject.get("test1"), "aa\"bb", "解析结果错误");
        Assertions.assertEquals(jsonObject.get("test2"), "   aa\"bb   ", "解析结果错误");
    }

    @Test
    public void test_parseComplexJson() {
        String json = "{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":[11,22,33,44,55],\"key4\":1,\"key5\":2,\"key6\":true,\"key7\":false,\"key8\":null,\"key9\":\"\",\"key10\":{\"sub_key1\":\"value1\",\"sub_key2\":\"value2\",\"sub_key3\":[11,22,33,44,55],\"sub_key4\":1,\"sub_key5\":2,\"sub_key6\":true,\"sub_key7\":false,\"sub_key8\":null,\"sub_key9\":\"\"}}";
        Object obj = new JsonTokener(json).parse();
        Assertions.assertNotNull(obj, "返回值为空");
        Assertions.assertEquals(obj.getClass(), JsonObject.class, "类型错误");
        JsonObject jsonObject = (JsonObject) obj;
        Assertions.assertEquals(jsonObject.get("key1"), "value1", "解析结果错误");
        Assertions.assertEquals(jsonObject.get("key2"), "value2", "解析结果错误");
        Object item = jsonObject.get("key3");
        Assertions.assertEquals(item.getClass(), JsonArray.class, "解析结果错误");
        JsonArray itemArray = (JsonArray) item;
        List<Object> list = itemArray.getList();
        Assertions.assertNotNull(list,"解析结果错误");
        Assertions.assertEquals(list.size(), 5,"解析结果错误");
        Assertions.assertEquals(list.get(0), new BigDecimal("11"),"解析结果错误");
        Assertions.assertEquals(list.get(1), new BigDecimal("22"),"解析结果错误");
        Assertions.assertEquals(list.get(2), new BigDecimal("33"),"解析结果错误");
        Assertions.assertEquals(list.get(3), new BigDecimal("44"),"解析结果错误");
        Assertions.assertEquals(list.get(4), new BigDecimal("55"),"解析结果错误");


        Assertions.assertEquals(jsonObject.get("key4"), new BigDecimal("1"), "解析结果错误");
        Assertions.assertEquals(jsonObject.get("key5"), new BigDecimal("2"), "解析结果错误");

        Assertions.assertEquals(jsonObject.get("key6"), true, "解析结果错误");
        Assertions.assertEquals(jsonObject.get("key7"), false, "解析结果错误");

        Assertions.assertNull(jsonObject.get("key8"), "解析结果错误");
        Assertions.assertEquals(jsonObject.get("key9"), "", "解析结果错误");

        Object subObj = jsonObject.get("key10");
        Assertions.assertNotNull(subObj, "解析结果错误");

        Assertions.assertEquals(subObj.getClass(), JsonObject.class, "解析结果错误");
        JsonObject subJsonObject = (JsonObject) subObj;
        Assertions.assertEquals(subJsonObject.get("sub_key1"), "value1", "解析结果错误");
        Assertions.assertEquals(subJsonObject.get("sub_key2"), "value2", "解析结果错误");
        Object subItem = subJsonObject.get("sub_key3");
        Assertions.assertEquals(subItem.getClass(), JsonArray.class, "解析结果错误");
        JsonArray subItemArray = (JsonArray) subItem;
        List<Object> subList = subItemArray.getList();
        Assertions.assertNotNull(subList,"解析结果错误");
        Assertions.assertEquals(subList.size(), 5,"解析结果错误");
        Assertions.assertEquals(subList.get(0), new BigDecimal("11"),"解析结果错误");
        Assertions.assertEquals(subList.get(1), new BigDecimal("22"),"解析结果错误");
        Assertions.assertEquals(subList.get(2), new BigDecimal("33"),"解析结果错误");
        Assertions.assertEquals(subList.get(3), new BigDecimal("44"),"解析结果错误");
        Assertions.assertEquals(subList.get(4), new BigDecimal("55"),"解析结果错误");


        Assertions.assertEquals(subJsonObject.get("sub_key4"), new BigDecimal("1"), "解析结果错误");
        Assertions.assertEquals(subJsonObject.get("sub_key5"), new BigDecimal("2"), "解析结果错误");

        Assertions.assertEquals(subJsonObject.get("sub_key6"), true, "解析结果错误");
        Assertions.assertEquals(subJsonObject.get("sub_key7"), false, "解析结果错误");

        Assertions.assertNull(subJsonObject.get("sub_key8"), "解析结果错误");
        Assertions.assertEquals(subJsonObject.get("sub_key9"), "", "解析结果错误");
    }

    @Test
    public void test_parseSimpleArrayJson() {
        String json = "[null, true, false, 1, 1.11, \"test\"]";
        Object obj = new JsonTokener(json).parse();
        Assertions.assertNotNull(obj, "返回值为空");
        Assertions.assertEquals(obj.getClass(), JsonArray.class, "类型错误");
        List<Object> list = ((JsonArray) obj).getList();
        Assertions.assertNotNull(list, "解析结果为空");
        Assertions.assertEquals(list.size(), 6, "解析结果大小错误");
        Assertions.assertNull(list.get(0), "解析结果错误");
        Assertions.assertEquals(list.get(1), true, "解析结果错误");
        Assertions.assertEquals(list.get(2), false, "解析结果错误");
        Assertions.assertEquals(list.get(3), new BigDecimal("1"), "解析结果错误");
        Assertions.assertEquals(list.get(4), new BigDecimal("1.11"), "解析结果错误");
        Assertions.assertEquals(list.get(5), "test", "解析结果错误");

    }
    @Test
    public void test_parseObjectArrayJson() {
        String json = "[{\"test_key0\": \"test_value0\"},{\"test_key1\": \"test_value1\"}, {\"test_key2\": \"test_value2\"}]";
        Object obj = new JsonTokener(json).parse();
        Assertions.assertNotNull(obj, "返回值为空");
        Assertions.assertEquals(obj.getClass(), JsonArray.class, "类型错误");
        JsonArray array = (JsonArray) obj;
        List<Object> list = array.getList();
        Assertions.assertNotNull(list, "解析结果为空");
        Assertions.assertEquals(list.size(), 3, "解析结果大小错误");
        for (int i = 0; i < list.size(); i++) {
            Object item = list.get(i);
            Assertions.assertNotNull(item.getClass(), "返回值为空");
            Assertions.assertEquals(item.getClass(), JsonObject.class, "类型错误");
            JsonObject jsonObject = (JsonObject) item;
            Assertions.assertEquals(jsonObject.get("test_key" + i), "test_value" + i, "解析结果错误");
        }

    }
}

package cn.chenzhen.wj;

import cn.chenzhen.wj.json.JsonArray;
import cn.chenzhen.wj.json.JsonConfig;
import cn.chenzhen.wj.json.JsonObject;
import cn.chenzhen.wj.json.JsonWrite;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TestJsonWrite {
    @Test
    public void testToJson() throws IOException {
        String tmp1 = new JsonWrite(null).toJson();
        Assertions.assertNull(tmp1, "结果错误");
        tmp1 = new JsonWrite("").toJson();
        Assertions.assertEquals(tmp1, "","结果错误");

        JsonObject jsonObject = new JsonObject();
        jsonObject.put("test", "val");
        JsonWrite write = new JsonWrite(jsonObject);
        String json = write.toJson();
        Assertions.assertEquals(json, "{\"test\":\"val\"}", "结果错误");
    }
    @Test
    public void testToJsonArray1() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(1);
        jsonArray.add(2);
        jsonArray.add(3);
        jsonArray.add(4);
        jsonArray.add(new BigDecimal("5"));
        jsonArray.add(new BigInteger("6"));
        JsonWrite write = new JsonWrite(jsonArray);
        String json = write.toJson();
        Assertions.assertEquals(json, "[1,2,3,4,5,6]", "结果错误");

        JsonConfig config = new JsonConfig();
        config.setNumberToStr(true);
        write = new JsonWrite(config, jsonArray);
        json = write.toJson();
        Assertions.assertEquals(json, "[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\"]", "结果错误");
    }

    @Test
    public void testToJsonArray2() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("test", "val");
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject);
        JsonWrite write = new JsonWrite(jsonArray);
        String json = write.toJson();
        Assertions.assertEquals(json, "[{\"test\":\"val\"}]", "结果错误");
    }

    @Test
    public void testToJsonArray3() throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonArray array = new JsonArray();
        array.add(1);
        array.add(2);
        array.add(3);
        jsonObject.put("test1", array);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(null);
        jsonArray.add(true);
        jsonArray.add(false);
        jsonObject.put("test2", jsonArray);
        jsonObject.put("test3", true);
        jsonObject.put("test4", false);
        jsonObject.put("test5", null);
        JsonWrite write = new JsonWrite(jsonObject);
        String json = write.toJson();
        Assertions.assertEquals(json, "{\"test1\":[1,2,3],\"test2\":[null,true,false],\"test3\":true,\"test4\":false,\"test5\":null}", "结果错误");
    }
}

package cn.chenzhen.wj;
import java.time.OffsetTime;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.LocalTime;

import cn.chenzhen.wj.json.JsonConfig;
import cn.chenzhen.wj.json.JsonUtil;
import cn.chenzhen.wj.json.bean.annotation.Json;
import cn.chenzhen.wj.json.bean.annotation.JsonUnwrapped;
import cn.chenzhen.wj.reflect.TypeReference;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TestJson {
    @Test
    public void test_SimpleType(){
        Data data = new Data();
        data.setData1(1);
        data.setData2(2);
        data.setData3((short)3);
        data.setData4((short)4);
        data.setData5(5L);
        data.setData6(6L);
        data.setData7(7.1D);
        data.setData8(8.2D);
        data.setData9(9.3F);
        data.setData10(10.4F);
        data.setData11(true);
        data.setData12(false);
        data.setData13('A');
        data.setData14('B');
        data.setData15((byte)'C');
        data.setData16((byte)'D');
        data.setData17("测试测试");
        data.setDate18(new Date());
        data.setDate19(new java.sql.Date(new Date().getTime()));
        data.setDate20(new Time(new Date().getTime()));
        data.setDate21(new Timestamp(new Date().getTime()));
        data.setDate22(Calendar.getInstance());
        data.setDate24(LocalDate.now());
        data.setDate25(LocalDateTime.now());
        data.setDate26(OffsetDateTime.now());
        data.setDate27(ZonedDateTime.now());
        data.setDate28(LocalTime.now());
        data.setDate29(OffsetTime.now());
        String json = JsonUtil.beanToJson(data);
        System.out.println(json);
        Data bean = JsonUtil.jsonToBean(json, Data.class);
        System.out.println(bean.getDate29());

    }
    @Test
    public void test_simpleTypeNull(){
        Data data = new Data();
        String json = JsonUtil.beanToJson(data);
        System.out.println(json);
        Data bean = JsonUtil.jsonToBean(json, Data.class);
        System.out.println(bean);

        JsonConfig config = new JsonConfig();
        config.setIgnoreNull(true);
        json = JsonUtil.beanToJson(data, config);
        System.out.println(json);
        bean = JsonUtil.jsonToBean(json, Data.class);
        System.out.println(bean);
    }
    @Test
    public void test_GenericBean() {
        Resp<User> resp = new Resp<>(200, "成功", new User("张珊","18"));
        resp.setDateTime(LocalDateTime.now());
        resp.setHead(new Head("0000","TEST_001"));
        String json = JsonUtil.beanToJson(resp);
        System.out.println(json);
        Resp<User> jsonBean = JsonUtil.jsonToBean(json, new TypeReference<Resp<User>>() {});
        System.out.println(jsonBean.getCode());
        System.out.println(jsonBean.getMsg());
        System.out.println(jsonBean.getDateTime());
        System.out.println(jsonBean.getHead().getStatus());
        System.out.println(jsonBean.getHead().getSerial());
        System.out.println(jsonBean.getData().getName());
        System.out.println(jsonBean.getData().getSex());
        System.out.println("---------------------------------------------------------");
        jsonBean = send(json, User.class);
        System.out.println(jsonBean.getCode());
        System.out.println(jsonBean.getMsg());
        System.out.println(jsonBean.getDateTime());
        System.out.println(jsonBean.getHead().getStatus());
        System.out.println(jsonBean.getHead().getSerial());
        System.out.println(jsonBean.getData().getName());
        System.out.println(jsonBean.getData().getSex());
    }
    public <T> Resp<T> send(String json, Class<T> clazz){
        return JsonUtil.jsonToBean(json, new TypeReference<Resp<T>>(clazz) {});
    }
    @Test
    public void test_Item(){
        Item<List<String>> item = new Item<List<String>>();
        item.setData(Arrays.asList("11","22"));
        String json = JsonUtil.beanToJson(item);
        System.out.println(json);
        Item<List<String>> bean = JsonUtil.jsonToBean(json, new TypeReference<Item<List<String>>>() {});
        System.out.println(bean.getData());
    }
}
class Item<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
class Data{
    private int data1;
    private Integer data2;
    private short data3;
    private Short data4;
    private long data5;
    private Long data6;
    private double data7;
    private Double data8;
    private float data9;
    private Float data10;
    private boolean data11;
    private Boolean data12;
    private char data13;
    private Character data14;
    private byte data15;
    private Byte data16;
    private String data17;
    private Date date18;
    private java.sql.Date date19;
    private java.sql.Time date20;
    private java.sql.Timestamp date21;
    private Calendar date22;
    private LocalDate date24;
    private LocalDateTime date25;
    private OffsetDateTime date26;
    private ZonedDateTime date27;
    private LocalTime date28;
    private OffsetTime date29;

    public int getData1() {
        return data1;
    }

    public void setData1(int data1) {
        this.data1 = data1;
    }

    public Integer getData2() {
        return data2;
    }

    public void setData2(Integer data2) {
        this.data2 = data2;
    }

    public short getData3() {
        return data3;
    }

    public void setData3(short data3) {
        this.data3 = data3;
    }

    public Short getData4() {
        return data4;
    }

    public void setData4(Short data4) {
        this.data4 = data4;
    }

    public long getData5() {
        return data5;
    }

    public void setData5(long data5) {
        this.data5 = data5;
    }

    public Long getData6() {
        return data6;
    }

    public void setData6(Long data6) {
        this.data6 = data6;
    }

    public double getData7() {
        return data7;
    }

    public void setData7(double data7) {
        this.data7 = data7;
    }

    public Double getData8() {
        return data8;
    }

    public void setData8(Double data8) {
        this.data8 = data8;
    }

    public float getData9() {
        return data9;
    }

    public void setData9(float data9) {
        this.data9 = data9;
    }

    public Float getData10() {
        return data10;
    }

    public void setData10(Float data10) {
        this.data10 = data10;
    }

    public boolean isData11() {
        return data11;
    }

    public void setData11(boolean data11) {
        this.data11 = data11;
    }

    public Boolean getData12() {
        return data12;
    }

    public void setData12(Boolean data12) {
        this.data12 = data12;
    }

    public char getData13() {
        return data13;
    }

    public void setData13(char data13) {
        this.data13 = data13;
    }

    public Character getData14() {
        return data14;
    }

    public void setData14(Character data14) {
        this.data14 = data14;
    }

    public byte getData15() {
        return data15;
    }

    public void setData15(byte data15) {
        this.data15 = data15;
    }

    public Byte getData16() {
        return data16;
    }

    public void setData16(Byte data16) {
        this.data16 = data16;
    }

    public String getData17() {
        return data17;
    }

    public void setData17(String data17) {
        this.data17 = data17;
    }

    public Date getDate18() {
        return date18;
    }

    public void setDate18(Date date18) {
        this.date18 = date18;
    }

    public java.sql.Date getDate19() {
        return date19;
    }

    public void setDate19(java.sql.Date date19) {
        this.date19 = date19;
    }

    public Time getDate20() {
        return date20;
    }

    public void setDate20(Time date20) {
        this.date20 = date20;
    }

    public Timestamp getDate21() {
        return date21;
    }

    public void setDate21(Timestamp date21) {
        this.date21 = date21;
    }

    public Calendar getDate22() {
        return date22;
    }

    public void setDate22(Calendar date22) {
        this.date22 = date22;
    }

    public LocalDate getDate24() {
        return date24;
    }

    public void setDate24(LocalDate date24) {
        this.date24 = date24;
    }

    public LocalDateTime getDate25() {
        return date25;
    }

    public void setDate25(LocalDateTime date25) {
        this.date25 = date25;
    }

    public OffsetDateTime getDate26() {
        return date26;
    }

    public void setDate26(OffsetDateTime date26) {
        this.date26 = date26;
    }

    public ZonedDateTime getDate27() {
        return date27;
    }

    public void setDate27(ZonedDateTime date27) {
        this.date27 = date27;
    }

    public LocalTime getDate28() {
        return date28;
    }

    public void setDate28(LocalTime date28) {
        this.date28 = date28;
    }

    public OffsetTime getDate29() {
        return date29;
    }

    public void setDate29(OffsetTime date29) {
        this.date29 = date29;
    }
}
class User{
    private String name;
    private String sex;

    public User() {
    }

    public User(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
class Head{
    private String status;
    private String serial;

    public Head() {
    }

    public Head(String status, String serial) {
        this.status = status;
        this.serial = serial;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}

class Resp<T>{
    private int code;
    @Json("message")
    private String msg;
    private T data;
    @Json(pattern = "yyyy年MM月dd日 HH时mm分ss秒")
    private LocalDateTime dateTime;
    @JsonUnwrapped
    private Head head;
    public Resp() {
    }

    public Resp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Resp(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }
}
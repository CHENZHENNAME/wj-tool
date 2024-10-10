package cn.chenzhen.wj.xml;

import cn.chenzhen.wj.reflect.TypeReference;
import cn.chenzhen.wj.xml.bean.annotation.Attr;
import cn.chenzhen.wj.xml.bean.annotation.Attribute;
import cn.chenzhen.wj.xml.bean.annotation.Xml;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class TestXml {
    @Test
    public void test_1() throws IOException {
        Item item = new Item("测试才是","哈哈哈");
        String xml = XmlUtil.beanToXml(item);
        System.out.println(xml);
        Item bean = XmlUtil.xmlToBean(xml, new TypeReference<Item>() {});
        System.out.println(bean.getName());
        System.out.println(bean.getAttr());

    }
    @Test
    public void test_2() throws IOException {
        Item2 item = new Item2("测试才是","哈哈哈");
        item.setData3(new Item3("11","22"));
        item.setData4(Arrays.asList(
                //new Item3("33","44"),
                new Item3("55","66")
        ));
        item.setData5(Arrays.asList(
                //new Item4("77","88"),
                new Item4("99","1010")
        ));

        String xml = XmlUtil.beanToXml(item);
        System.out.println(xml);
        Item2 bean = XmlUtil.xmlToBean(xml, Item2.class);
        System.out.println(bean.getData1());
        System.out.println(bean.getData2());
        System.out.println(bean.getData3().getData1());
        System.out.println(bean.getData3().getData2());
        System.out.println(bean.getData4().get(0).getData1());
        System.out.println(bean.getData4().get(0).getData2());

        System.out.println(bean.getData5().get(0).getData1());
        System.out.println(bean.getData5().get(0).getData2());

    }
    @Test
    public void test_3() throws IOException {
        Item2 item = new Item2(null,"哈哈哈");
        item.setData3(new Item3("11","22"));
        item.setData4(Arrays.asList(
                new Item3("33","44"),
                new Item3("55","66")
        ));
        item.setData5(Arrays.asList(
                new Item4("77","88"),
                new Item4("99","1010")
        ));

        XmlConfig config = new XmlConfig();
        config.setIgnoreEmptyTag(true);
        String xml = XmlUtil.beanToXml(item, config);
        System.out.println(xml);
        Item2 bean = XmlUtil.xmlToBean(xml, Item2.class);
        System.out.println(bean.getData1());
        System.out.println(bean.getData2());
        System.out.println(bean.getData3().getData1());
        System.out.println(bean.getData3().getData2());
        System.out.println(bean.getData4().get(0).getData1());
        System.out.println(bean.getData4().get(0).getData2());
        System.out.println(bean.getData4().get(1).getData1());
        System.out.println(bean.getData4().get(1).getData2());

        System.out.println(bean.getData5().get(0).getData1());
        System.out.println(bean.getData5().get(0).getData2());
        System.out.println(bean.getData5().get(1).getData1());
        System.out.println(bean.getData5().get(1).getData2());

    }
}

@Xml("Root")
class Item2{
    private String data1;
    private String data2;
    @Xml("DATA_OBJ")
    private Item3 data3;
    private List<Item3> data4;
    private List<Item4> data5;


    public Item2() {
    }

    public Item2(String data1, String data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public Item3 getData3() {
        return data3;
    }

    public void setData3(Item3 data3) {
        this.data3 = data3;
    }

    public List<Item3> getData4() {
        return data4;
    }

    public void setData4(List<Item3> data4) {
        this.data4 = data4;
    }

    public List<Item4> getData5() {
        return data5;
    }

    public void setData5(List<Item4> data5) {
        this.data5 = data5;
    }

}
@Xml("DATA_ITEM")
class Item3{
    private String data1;
    private String data2;
    public Item3() {}
    public Item3(String data1, String data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }
}
class Item4{
    private String data1;
    private String data2;
    public Item4() {}
    public Item4(String data1, String data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }
}

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

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, String attr) {
        this.name = name;
        this.attr = attr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }
}
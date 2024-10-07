package cn.chenzhen.wj.xml;

import cn.chenzhen.wj.reflect.TypeReference;
import cn.chenzhen.wj.xml.bean.annotation.Attr;
import cn.chenzhen.wj.xml.bean.annotation.Attribute;
import cn.chenzhen.wj.xml.bean.annotation.Xml;
import org.junit.jupiter.api.Test;

import java.io.*;

public class TestXml {
    @Test
    public void test() throws IOException {
        Item item = new Item("测试才是","哈哈哈");
        String xml = XmlUtil.beanToXml(item);
        System.out.println(xml);
        Item bean = XmlUtil.xmlToBean(xml, new TypeReference<Item>() {});
        System.out.println(bean.getName());
        System.out.println(bean.getAttr());

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
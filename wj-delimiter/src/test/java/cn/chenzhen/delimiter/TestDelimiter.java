package cn.chenzhen.delimiter;

import cn.chenzhen.wj.delimiter.CsvUtil;
import cn.chenzhen.wj.delimiter.DelimiterUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestDelimiter {
    @Test
    public void test() {
        Item item = new Item("测试", "哈哈");
        String text = DelimiterUtil.beanToText(item);
        System.out.println(text);
        Item bean = DelimiterUtil.textToBean(text, Item.class);
        System.out.println(bean.getName());
        System.out.println(bean.getSex());
    }
    @Test
    public void test_CSV() {
        Item item = new Item("测,,试", "哈,,哈");
        String text = CsvUtil.beanToCsv(item);
        System.out.println(text);
        Item bean = CsvUtil.csvToBean(text, Item.class);
        System.out.println(bean.getName());
        System.out.println(bean.getSex());
    }
}

class Item{
    private String name;
    private String sex;

    public Item() {
    }

    public Item(String name, String sex) {
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

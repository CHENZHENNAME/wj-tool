package cn.chenzhen.wj.fix;

import cn.chenzhen.wj.fix.annotation.Fix;
import org.junit.jupiter.api.Test;


public class TestFix {
    @Test
    public void test_T(){
        Item item = new Item();
        item.setData1("11");
        item.setData2("22");
        item.setName("33");
        item.setSex("44");
        byte[] bytes = FixUtil.beanToFix(item);
        System.out.println(new String(bytes));
        Item bean = FixUtil.fixToBean(bytes, Item.class);
        System.out.println(bean.getData1());
        System.out.println(bean.getData2());
        System.out.println(bean.getName());
        System.out.println(bean.getSex());

    }
}

class Parent {
    @Fix(10)
    private String data1;
    @Fix(20)
    private String data2;

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
class Item extends Parent{
    @Fix(30)
    private String name;
    @Fix(10)
    private String sex;

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
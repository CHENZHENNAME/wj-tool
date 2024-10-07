package cn.chenzhen.wj.xml;


import cn.chenzhen.wj.reflect.TypeReference;
import cn.chenzhen.wj.xml.bean.BeanToXmlObject;
import cn.chenzhen.wj.xml.bean.XmlObjectToBean;

public class XmlUtil {
    /**
     * xml字符串转换为 目标对象
     * @param xml 字符串
     * @param clazz 目标对象类型
     * @return 目标对象
     */
    public static <T> T xmlToBean(String xml, Class<T> clazz) {
        return xmlToBean(xml, XmlConfig.global(), clazz);
    }
    /**
     * xml字符串转换为 目标对象
     * @param xml 字符串
     * @param config 配置
     * @param clazz 目标对象类型
     * @return 目标对象
     */
    public static <T> T xmlToBean(String xml, XmlConfig config, Class<T> clazz) {
        XmlObject obj = xmlToXmlObject(xml, config);
        return new XmlObjectToBean(config).toBean(obj, clazz);
    }
    /**
     * xml字符串转换为 目标对象
     * @param xml 字符串
     * @param typeReference 目标对象引用
     * @return 目标对象
     */
    public static <T> T xmlToBean(String xml, TypeReference<T> typeReference) {
        return xmlToBean(xml, XmlConfig.global(), typeReference);
    }

    /**
     * xml字符串转换为 目标对象
     * @param xml 字符串
     * @param config 配置
     * @param typeReference 目标对象引用
     * @return 目标对象
     */
    public static <T> T xmlToBean(String xml, XmlConfig config, TypeReference<T> typeReference) {
        XmlObject obj = xmlToXmlObject(xml, config);
        return new XmlObjectToBean(config).toBean(obj, typeReference);
    }


    /**
     * 对象转换为 xml字符串
     * @param bean 对象
     * @return xml字符串
     */
    public static String beanToXml(Object bean) {
        return beanToXml(bean, XmlConfig.global());
    }

    /**
     * 对象转换为 xml字符串
     * @param bean 对象
     * @param config 配置
     * @return xml字符串
     */
    public static String beanToXml(Object bean, XmlConfig config) {
        XmlObject object = new BeanToXmlObject(config).toXmlObject(bean);
        return xmlObjectToString(object, config);
    }

    /**
     * xml字符串解析为 XmlObject对象
     * @param xml 字符串
     * @return XmlObject对象
     */
    public static XmlObject xmlToXmlObject(String xml) {
        return xmlToXmlObject(xml, XmlConfig.global());
    }
    /**
     * xml字符串解析为 XmlObject对象
     * @param xml 字符串
     * @param config 配置
     * @return XmlObject对象
     */
    public static XmlObject xmlToXmlObject(String xml, XmlConfig config) {
        try (XmlTokener token = new XmlTokener(xml)){
            return token.parse();
        }catch (Exception e){
            throw new XmlException(e);
        }
    }

    /**
     * XmlObject转换为xml字符串
     * @param xmlObject xml对象
     * @return xml字符串
     */
    public static String xmlObjectToString(XmlObject xmlObject) {
        return xmlObjectToString(xmlObject, XmlConfig.global());
    }
    /**
     * XmlObject转换为xml字符串
     * @param xmlObject xml对象
     * @param config 配置
     * @return xml字符串
     */
    public static String xmlObjectToString(XmlObject xmlObject, XmlConfig config) {
        try (XmlWrite write = new XmlWrite(config)){
            return write.toXml(xmlObject);
        }catch (Exception e){
            throw new XmlException(e);
        }
    }
}

package cn.chenzhen.wj.xml;

import cn.chenzhen.wj.type.convert.DateUtil;
import cn.chenzhen.wj.type.convert.TypeUtil;
import cn.chenzhen.wj.util.UnicodeUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class XmlWrite implements AutoCloseable{
    private static final byte XML_TAG_LT = '<';
    private static final byte XML_TAG_GT = '>';
    private static final byte XML_TAG_SLASH = '/';
    private static final byte XML_TAG_EMPTY = ' ';
    private static final byte XML_TAG_EQ = '=';
    private static final byte XML_TAG_DQM = '"';
    private static final Map<Class<?>, WriteValue> WRITE_VALUE_MAP = new ConcurrentHashMap<>();

    static {
        // 日期处理
        WRITE_VALUE_MAP.put(Calendar.class, (data, config) -> DateUtil.format(data, config.getDateTimePattern()));
        WRITE_VALUE_MAP.put(Date.class, (data, config) -> DateUtil.format(data, config.getDateTimePattern()));
        WRITE_VALUE_MAP.put(LocalDate.class, (data, config) -> DateUtil.format(data, config.getDatePattern()));
        WRITE_VALUE_MAP.put(LocalDateTime.class, (data, config) -> DateUtil.format(data, config.getDateTimePattern()));
        WRITE_VALUE_MAP.put(OffsetDateTime.class, (data, config) -> DateUtil.format(data, config.getDateTimePattern()));
        WRITE_VALUE_MAP.put(ZonedDateTime.class, (data, config) -> DateUtil.format(data, config.getDateTimePattern()));
        WRITE_VALUE_MAP.put(LocalTime.class, (data, config) -> DateUtil.format(data, config.getTimePattern()));
        WRITE_VALUE_MAP.put(OffsetTime.class, (data, config) -> DateUtil.format(data, config.getTimePattern()));
        WRITE_VALUE_MAP.put(java.sql.Date.class, (data, config) -> DateUtil.format(data, config.getDateTimePattern()));
        WRITE_VALUE_MAP.put(java.sql.Time.class, (data, config) -> DateUtil.format(data, config.getDateTimePattern()));
        WRITE_VALUE_MAP.put(java.sql.Timestamp.class, (data, config) -> DateUtil.format(data, config.getDateTimePattern()));
    }

    interface WriteValue {
        String serialize(Object data, XmlConfig config);
    }

    private XmlConfig config;
    private StringWriter out;

    public XmlWrite() {
        this(new XmlConfig());
    }

    public XmlWrite(XmlConfig config) {
        this.config = config;
        out = new StringWriter();
    }
    public String toXml(XmlObject xml){
        try {
            if (xml == null) {
                return null;
            }
            Map.Entry<String, Object> rootXml = xml.getFirstNode();
            xmlObjectToString(rootXml.getKey(), rootXml.getValue());
            return out.toString();
        } catch (IOException e) {
            throw new XmlException(e);
        }
    }

    private void xmlObjectToString(String xmlName, Object xml) throws IOException {
        if (xml == null) {
            buildXmlTag(xmlName, null);
            return;
        }
        // 基本类型
        Object val = convertFiledValue(xml);
        if (val != null) {
            buildXmlTag(xmlName, String.valueOf(val));
            return;
        }
        // 数组标签 xml一个标签可以出现多次
        if (xml instanceof Iterable) {
            for (Object obj : (Iterable<?>) xml) {
                xmlObjectToString(xmlName, obj);
            }
            return;
        }
        // xml非类型
        if (!(xml instanceof XmlObject)) {
            throw new XmlException("error type " + xml.getClass());
        }
        XmlObject xmlObject = (XmlObject) xml;

        out.write(XML_TAG_LT);
        out.write(xmlName);
        buildAttribute(xmlObject);
        Map<String, Object> nodes = xmlObject.getNodes();
        Object value = xmlObject.getValue();
        val = convertFiledValue(value);
        if ((nodes == null || nodes.isEmpty()) && (val == null || val.toString().isEmpty()) && config.isSimpleTag()) {
            out.write(XML_TAG_SLASH);
            out.write(XML_TAG_GT);
            return;
        }
        out.write(XML_TAG_GT);
        if (val != null) {
            out.write(val.toString());
        }

        buildNodes(nodes);
        buildXmlTagEnd(xmlName);

    }
    private void buildNodes(Map<String, Object> nodes) throws IOException {
        if (nodes == null) {
            return;
        }
        Set<Map.Entry<String, Object>> entrySet = nodes.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            xmlObjectToString(entry.getKey(), entry.getValue());
        }
    }
    private void buildAttribute(XmlObject xml) {
        Map<String, String> map = xml.getAttributes();
        if (map == null || map.isEmpty()) {
            return;
        }
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            out.write(XML_TAG_EMPTY);
            out.write(entry.getKey());
            out.write(XML_TAG_EQ);
            out.write(XML_TAG_DQM);
            out.write( entry.getValue());
            out.write(XML_TAG_DQM);
        }
    }
    private String convertFiledValue(Object value){
        if (value == null) {
            return null;
        }
        Object val = TypeUtil.serialize(value);
        if (val != null) {
            return val.toString();
        }
        WriteValue writeValue = WRITE_VALUE_MAP.get(value.getClass());
        if (writeValue != null) {
            return writeValue.serialize(value, config);
        }
        if (value instanceof Calendar) {
            return WRITE_VALUE_MAP.get(Calendar.class).serialize(value, config);
        }
        return null;
    }


    /**
     * 生成xml标签
     * @param name 标签名称
     * @param value 表值
     */
    private void buildXmlTag(String name, String value) throws IOException {
        if (value == null && config.isSimpleTag()) {
            out.write(XML_TAG_LT);
            out.write(name);
            out.write(XML_TAG_SLASH);
            out.write(XML_TAG_GT);
            return;
        }
        buildXmlTagStart(name);
        if (value != null) {
            writeString(value);
        }
        buildXmlTagEnd(name);
    }
    private void buildXmlTagStart(String xmlName) throws IOException {
        out.write(XML_TAG_LT);
        out.write(xmlName);
        out.write(XML_TAG_GT);
    }
    private void buildXmlTagEnd(String xmlName) throws IOException {
        out.write(XML_TAG_LT);
        out.write(XML_TAG_SLASH);
        out.write(xmlName);
        out.write(XML_TAG_GT);
    }
    /**
     * 字符串特殊字符处理
     * @param value 值
     */
    public void writeString(String value) {
        boolean escapeFlag = config.isEscape();
        char[] array = value.toCharArray();
        for (char c : array) {
            switch (c) {
                case '<':
                    // <  &lt;
                    out.write("&lt;");
                    continue;
                case '>':
                    // >  &gt;
                    out.write("&gt;");
                    continue;
                case '"':
                    // "  &quot;
                    out.write("&quot;");
                    continue;
                case '&':
                    // &  &amp;
                    out.write("&amp;");
                    continue;
                case '\'':
                    // '  &apos;
                    out.write("&apos;");
                    continue;
                default:
                    if (escapeFlag && !UnicodeUtil.checkCode(c) ) {
                        String code = "&#" + (int) c + ";";
                        out.write(code);
                    } else {
                        out.write(c);
                    }
            }

        }
    }

    @Override
    public void close() throws Exception {
        out.close();
    }

}

package cn.chenzhen.wj.xml;

import java.io.*;

public class XmlTokener implements AutoCloseable {
    private static final int EOF = -1;
    private static final int EMPTY = ' ';
    public static final int TAB = '\t';
    private static final int LF = '\n';
    private static final int XML_TAG_LT = '<';
    private static final int XML_TAG_GT = '>';
    private static final int XML_TAG_SB_START = '[';
    private static final int XML_TAG_SB_END = ']';
    private static final int XML_TAG_QM = '?';
    private static final int XML_TAG_EQ = '=';
    private static final int XML_TAG_DQM = '"';
    private static final int XML_TAG_SLASH = '/';
    private static final int XML_TAG_MS = '-';
    private static final int XML_TAG_EM = '!';

    private Reader reader;
    private boolean back;
    private int previousCode;
    private int currentCode;
    /**
     * 当前行
     */
    private int currentRow = 1;
    /**
     * 当前列
     */
    private int currentColumn = 0;
    public XmlTokener(InputStream is) {
        this.reader = new InputStreamReader(is);
    }

    public XmlTokener(String xml) {
        reader = new StringReader(xml);
    }
    public XmlObject parse(){
        int code = nextToken();
        assertCode(code, XML_TAG_LT);
        back();
        parseHead();
        XmlObject root = new XmlObject();
        parseNode(root);
        return root;
    }

    private void parseNode(XmlObject parent) {
        while (true) {
            int code = nextToken();
            // 注释 或者 CDATA
            if (code == XML_TAG_EM) {
                code = nextToken();
                if (code == XML_TAG_MS) {
                    // 注释
                    parseXmlAnnotation();
                    code = nextToken();
                    if (code == XML_TAG_LT){
                        continue;
                    }
                    back();
                    // 文本标签
                    String txt = readToTag(XML_TAG_LT);
                    parent.setValue(txt);
                    return;
                } else if (code == XML_TAG_SB_START) {
                    // CDATA 文本
                    String txt = parseXmlCDATA();
                    parent.setValue(txt);
                }
                return;
            }
            back();
            // 注释、标签
            XmlObject node = new XmlObject();
            // <xml xx="xx"> 或者 <xml> 或者 <xml/>
            String tag = readXmlTag();
            parent.appendNode(tag, node);
            readeAttributes(node);
            code = nextToken();
            // 单标签
            if (code == XML_TAG_SLASH) {
                code = nextToken();
                assertCode(code, XML_TAG_GT);
                continue;
            }
            assertCode(code, XML_TAG_GT);
            // 子节点处理
            while (true) {
                // 可能是子标签 也可能是文本节点  也可能是空节点  <![CDATA[ xxxx ]]>
                code = nextToken();
                if (code != XML_TAG_LT) {
                    back();
                    // 文本标签
                    String txt = readToTag(XML_TAG_LT);
                    node.setValue(txt);
                }
                code = nextToken();
                if (code == XML_TAG_SLASH) {
                    // 结束标签
                    String tagEnd = readToTag(XML_TAG_GT);
                    if (!tagEnd.equals(tag)) {
                        throw error("Unexpected end of expect tag: " + tag + " actual tag: " + tagEnd);
                    }
                    return;
                } else {
                    back();
                    parseNode(node);
                }
            }
        }
    }

    private String parseXmlCDATA(){
        // <![CDATA[aaaaaaaa]]>
        String tag = readToTag(XML_TAG_SB_START);
        if (!"CDATA".equals(tag)) {
            throw error("Expected CDATA but found " + tag);
        }
        String txt = readToTag(XML_TAG_SB_END);
        return txt;

    }
    /**
     * 读取注释标签
     * @return 注释内容
     */
    private String parseXmlAnnotation(){
        int code = nextToken();
        assertCode(code, XML_TAG_MS);
        String annotation = readToTag(XML_TAG_MS);
        code = nextToken();
        assertCode(code, XML_TAG_GT);
        return annotation;
    }


    /**
     * 读取Xml标签上的所有属性
     * @param xml 节点
     */
    private void readeAttributes(XmlObject xml){
        back();
        int code = nextToken();
        back();
        if (tagEnd(code)) {
            return;
        }
        while (true){
            String key = readToTag(XML_TAG_EQ);
            code = nextToken();
            assertCode(code, XML_TAG_DQM);
            String val = readToTag(XML_TAG_DQM);
            xml.appendAttribute(key, val);
            code = nextToken();
            back();
            if (tagEnd(code)) {
                break;
            }
        }
    }
    private boolean tagEnd(int code){
        //  结束标签 或者 单标签的情况 如：<div/>
        if (code == XML_TAG_GT || code == XML_TAG_SLASH){
            return true;
        }
        return false;
    }
    private String readXmlTag(){
        StringBuilder tag = new StringBuilder();
        label:
        while (true){
            int code = next();
            switch (code) {
                case XML_TAG_GT:
                case XML_TAG_SLASH:
                case EMPTY:
                    break label;
            }
            tag.append((char) code);
        }
        return tag.toString();
    }
    /**
     * 解析 xml 头
     */
    private void parseHead(){
        int code = nextToken();
        assertCode(code, XML_TAG_LT);
        code = nextToken();
        if (code != XML_TAG_QM) {
            back();
            return;
        }
        // <?xml version="1.0" encoding="UTF-8"?>
        String headTag = readToTag(EMPTY);
        String headVersionKey = readToTag(XML_TAG_EQ);
        code = nextToken();
        assertCode(code, XML_TAG_DQM);
        String headVersionVal = readToTag(XML_TAG_DQM);
        System.out.println(headVersionKey + " = " + headVersionVal);
        code = nextToken();
        // encoding 属性为可选 需要判断
        if (code != XML_TAG_QM) {
            back();
            String headEncodingKey = readToTag(XML_TAG_EQ);
            code = nextToken();
            assertCode(code, XML_TAG_DQM);
            String headEncodingVal = readToTag(XML_TAG_DQM);
            System.out.println(headEncodingKey + " = " + headEncodingVal);
        }
        code = nextToken();
        assertCode(code, XML_TAG_GT);
    }
    private void back() {
        if (back) {
            throw new XmlException("Already rolled back");
        }
        currentColumn--;
        back = true;
    }

    private String readToTag(int tag){
        StringBuilder val = new StringBuilder();
        while(true){
            int code = next();
            if (tag == code) {
                if (tag == XML_TAG_MS) {
                    int nextCode = next();
                    // 存在注释的情况 <---1-2-3-->
                    if (nextCode == tag) {
                        break;
                    }
                    val.append((char) code);
                    code = nextCode;

                } else if (tag == XML_TAG_SB_END) {
                    // ]]>
                    int nextCode1 = next();
                    int nextCode2 = next();
                    if (nextCode1 == tag && nextCode2 == XML_TAG_GT) {
                        break;
                    }
                    val.append((char) code);
                    val.append((char) nextCode1);
                    code = nextCode2;
                } else {
                    break;
                }

            }
            if (code == EOF) {
                throw new XmlException("Unexpected end of file");
            }
            val.append((char) code);
        }
        return val.toString().trim();
    }
    public int nextToken(){
        while(true){
            int code = next();
            switch(code){
                case TAB:
                case LF:
                case EMPTY:
                    continue;
                default:
                    return code;
            }
        }
    }
    public int next(){
        try {
            if (back){
                back = false;
                currentColumn++;
                return currentCode;
            }
            previousCode = currentCode;
            int code = reader.read();
            if (code == LF) {
                currentRow++;
                currentColumn = 0;
            }
            currentCode = code;
            return code;
        } catch (IOException e) {
            throw new XmlException(e);
        }
    }
    private void assertCode(int code, int expected){
        if(code != expected){
            throw error("Expected " + (char)expected + " but got " + (char)code);
        }
    }
    private XmlException error(String msg){
        throw new XmlException("row " + currentRow + " column " + currentColumn + ": " + msg );
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
}

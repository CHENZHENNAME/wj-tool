package cn.chenzhen.wj.type.convert.service;

public class StringService implements ConvertService<String> {
    @Override
    public Class<String> type() {
        return String.class;
    }

    @Override
    public String deserializer(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}

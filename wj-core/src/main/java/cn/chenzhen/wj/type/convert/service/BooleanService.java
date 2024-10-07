package cn.chenzhen.wj.type.convert.service;

import cn.chenzhen.wj.type.convert.TypeException;


public class BooleanService implements ConvertService<Boolean> {
    @Override
    public Class<Boolean> type() {
        return Boolean.class;
    }

    @Override
    public Boolean deserializer(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if (value instanceof Byte) {
            return (Byte) value == 1;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }

        if (value instanceof Character) {
            return (int) value == 1;
        }
        if (value instanceof String) {
            return Integer.parseInt(value.toString()) == 1;
        }
        throw new TypeException("can not cast to boolean, value : " + value);
    }
}

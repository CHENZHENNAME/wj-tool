package cn.chenzhen.wj.type.convert.service;

import cn.chenzhen.wj.type.convert.TypeException;


public class ByteService implements ConvertService<Byte> {
    @Override
    public Class<Byte> type() {
        return Byte.class;
    }

    @Override
    public Byte deserializer(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Byte) {
            return (Byte) value;
        }
        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }

        if (value instanceof Boolean) {
            return (Boolean) value ? (byte) 1 : (byte) 0;
        }
        if (value instanceof Character) {
            return (byte) value;
        }
        if (value instanceof String) {
            return (byte)Integer.parseInt(value.toString());
        }
        throw new TypeException("can not cast to byte, value : " + value);
    }
}

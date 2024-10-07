package cn.chenzhen.wj.type.convert.service;

import cn.chenzhen.wj.type.convert.TypeException;

public class ShortService implements ConvertService<Short> {
    @Override
    public Class<Short> type() {
        return Short.class;
    }

    @Override
    public Short deserializer(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Byte || value instanceof Character) {
            return (short) value;
        }
        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }
        
        if (value instanceof Boolean) {
            return (Boolean) value ? (short)1 :  (short)0;
        }

        if (value instanceof String) {
            return Short.parseShort(value.toString());
        }
        throw new TypeException("can not cast to short, value : " + value);
    }
}

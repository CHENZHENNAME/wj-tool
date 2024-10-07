package cn.chenzhen.wj.type.convert.service;

import cn.chenzhen.wj.type.convert.TypeException;

public class LongService implements ConvertService<Long> {
    @Override
    public Class<Long> type() {
        return Long.class;
    }

    @Override
    public Long deserializer(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Byte || value instanceof Character) {
            return (long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        
        if (value instanceof Boolean) {
            return (Boolean) value ? 1L : 0L;
        }
        if (value instanceof String) {
            return Long.parseLong(value.toString());
        }
        throw new TypeException("can not cast to long, value : " + value);
    }
}

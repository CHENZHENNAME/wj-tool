package cn.chenzhen.wj.type.convert.service;

import cn.chenzhen.wj.type.convert.TypeException;


public class IntService implements ConvertService<Integer> {
    @Override
    public Class<Integer> type() {
        return Integer.class;
    }

    @Override
    public Integer deserializer(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Byte || value instanceof Character) {
            return (int) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        
        if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        }

        if (value instanceof String) {
            return Integer.parseInt(value.toString());
        }
        throw new TypeException("can not cast to int, value : " + value);
    }
}

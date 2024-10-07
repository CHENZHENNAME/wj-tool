package cn.chenzhen.wj.type.convert.service;

import cn.chenzhen.wj.type.convert.TypeException;


public class DoubleService implements ConvertService<Double> {
    @Override
    public Class<Double> type() {
        return Double.class;
    }

    @Override
    public Double deserializer(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Byte || value instanceof Character) {
            return (double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        
        if (value instanceof Boolean) {
            return (Boolean) value ? 1.0 : 0;
        }

        if (value instanceof String) {
            return Double.parseDouble(value.toString());
        }
        throw new TypeException("can not cast to double, value : " + value);
    }
}

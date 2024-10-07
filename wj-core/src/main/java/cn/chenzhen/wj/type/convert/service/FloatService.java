package cn.chenzhen.wj.type.convert.service;

import cn.chenzhen.wj.type.convert.TypeException;


public class FloatService implements ConvertService<Float> {
    @Override
    public Class<Float> type() {
        return Float.class;
    }

    @Override
    public Float deserializer(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Byte || value instanceof Character) {
            return (float) value;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        
        if (value instanceof Boolean) {
            return (Boolean) value ? (float)1 :  (float)0;
        }
        if (value instanceof String) {
            return Float.parseFloat(value.toString());
        }
        throw new TypeException("can not cast to float, value : " + value);
    }
}

package cn.chenzhen.wj.type.convert.service;


import cn.chenzhen.wj.type.convert.TypeException;

import java.math.BigDecimal;

public class BigDecimalService implements ConvertService<BigDecimal>{
    @Override
    public Class<BigDecimal> type() {
        return BigDecimal.class;
    }

    @Override
    public BigDecimal deserializer(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }
        if (value instanceof String) {
            return new BigDecimal((String) value);
        }
        throw new TypeException("can not cast to BigDecimal, value : " + value);
    }

    @Override
    public Object serialize(BigDecimal value) {
        return value.toPlainString();
    }
}

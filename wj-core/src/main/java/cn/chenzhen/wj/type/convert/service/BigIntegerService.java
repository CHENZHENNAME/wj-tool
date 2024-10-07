package cn.chenzhen.wj.type.convert.service;


import cn.chenzhen.wj.type.convert.TypeException;

import java.math.BigInteger;

public class BigIntegerService implements ConvertService<BigInteger>{
    @Override
    public Class<BigInteger> type() {
        return BigInteger.class;
    }

    @Override
    public BigInteger deserializer(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        if (value instanceof Number) {
            return new BigInteger(value.toString());
        }
        if (value instanceof String) {
            return new BigInteger((String) value);
        }
        throw new TypeException("can not cast to BigInteger, value : " + value);
    }

    @Override
    public Object serialize(BigInteger value) {
        try {
            return value.intValueExact();
        } catch (Exception e) {
            return value.longValueExact();
        }
    }
}

package cn.chenzhen.wj.type.convert.service;

import cn.chenzhen.wj.type.convert.TypeException;


public class CharService implements ConvertService<Character> {
    @Override
    public Class<Character> type() {
        return Character.class;
    }

    @Override
    public Character deserializer(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Character) {
            return (Character) value;
        }
        if (value instanceof Byte) {
            return (char) value;
        }
        if (value instanceof Number) {
            return (char)((Number) value).intValue();
        }
        
        if (value instanceof Boolean) {
            return (Boolean) value ? (char)1 :  (char)0;
        }
        if (value instanceof String) {
            String str = (String) value;
            if (str.length() != 1) {
                throw new TypeException("can not cast to char, value : " + value);
            }
            return str.charAt(0);
        }
        throw new TypeException("can not cast to char, value : " + value);
    }
}

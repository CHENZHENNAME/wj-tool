package cn.chenzhen.wj.type;

import cn.chenzhen.wj.reflect.ClassUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BeanClassTypeFactory {
    /**
     * 类型映射
     */
    private static final Map<Class<?>, Class<?>> TYPE_TARGET = new ConcurrentHashMap<>();
    static {

        TYPE_TARGET.put(List.class, ArrayList.class);
        TYPE_TARGET.put(AbstractList.class, ArrayList.class);
        TYPE_TARGET.put(Set.class, HashSet.class);
        TYPE_TARGET.put(Map.class, HashMap.class);
        TYPE_TARGET.put(AbstractMap.class, HashMap.class);
    }
    public static void registerType(Class<?> source,Class<?> target) {
        TYPE_TARGET.put(source, target);
    }

    /**
     * 创建对象
     * @param clazz 对象
     * @return 实例对象
     */
    public static Object createBean(Class<?> clazz) {
        Class<?> target = TYPE_TARGET.get(clazz);
        if (target == null) {
            target = clazz;
        }
        return ClassUtil.newConstructor(target);
    }
}

package cn.chenzhen.wj.json.bean.annotation.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注解信息处理工厂 方便后续扩展自定义注解
 */
public class AnnotationProcessorFactory {
    private static final Map<Class<?>, AnnotationProcessor<?>> ANNOTATION_PROCESSOR_MAP = new ConcurrentHashMap<>();

    static {
        register(new JsonAnnotationProcessor());
        register(new JsonUnwrappedAnnotationProcessor());
    }
    /**
     * 注册主机信息处理
     * @param processor 处理器
     */
    public static void register(AnnotationProcessor<? extends Annotation> processor) {
        ParameterizedType pt = (ParameterizedType) processor.getClass().getGenericInterfaces()[0];
        Class<?> type = (Class<?>) pt.getActualTypeArguments()[0];
        ANNOTATION_PROCESSOR_MAP.put(type, processor);
    }
    @SuppressWarnings("unchecked")
    public static<T extends Annotation> AnnotationProcessor<T> getAnnotationProcessor(T annotation) {
        return (AnnotationProcessor<T>) ANNOTATION_PROCESSOR_MAP.get(annotation.annotationType());
    }
}

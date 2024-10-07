package cn.chenzhen.wj.xml.bean.annotation.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationProcessorFactory {
    private static final Map<Class<?>, AnnotationProcessor<?>> ANNOTATION_PROCESSOR_MAP = new ConcurrentHashMap<>();
    static {
        register(new XmlAnnotationProcessor());
        register(new AttrAnnotationProcessor());
        register(new AttributeAnnotationProcessor());
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
    public static AnnotationProcessor<Annotation> getAnnotationProcessor(Annotation annotation) {
        return (AnnotationProcessor<Annotation>)ANNOTATION_PROCESSOR_MAP.get(annotation.annotationType());
    }
}

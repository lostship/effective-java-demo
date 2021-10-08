package com.example.effectivejava.ch5generic.i33heterogeneouscontainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class AnonationTest {
    /**
     * 这里被注解的元素AnnotatedElement本质上是一个类型安全的异构容器，容器的键是注解类型。
     */
    public static <T extends Annotation> T getAnnotation(AnnotatedElement element, Class<T> annotationClass) {
        return element.getAnnotation(annotationClass);
    }

    public static Annotation getAnnotation(AnnotatedElement element, String annotationClassName)
            throws ClassNotFoundException {
        // 这种方式会得到unchecked警告
        // Class<? extends Annotation> cl = (Class<? extends Annotation>) Class.forName(annotationClassName);

        // asSubClass方法可以将Class对象转换成参数表示的类的一个子类
        Class<?> cl = Class.forName(annotationClassName);
        Class<? extends Annotation> subClass = cl.asSubclass(Annotation.class);
        return getAnnotation(element, subClass);
    }
}

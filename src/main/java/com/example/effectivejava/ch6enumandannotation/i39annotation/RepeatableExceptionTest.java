package com.example.effectivejava.ch6enumandannotation.i39annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method is a test method that
 * must throw the designated exception to succeed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(RepeatableExceptionTestContainer.class)
public @interface RepeatableExceptionTest {
    Class<? extends Throwable> value();
}

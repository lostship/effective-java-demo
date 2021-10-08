package com.example.effectivejava.ch6enumandannotation.i39annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RunTests {
    public static void main(String[] args) {
        test(Sample2.class);
    }

    public static void test(Class<?> testClass) {
        int tests = 0;
        int passed = 0;
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                tests++;
                try {
                    m.invoke(null);
                    passed++;
                } catch (InvocationTargetException wrappedExc) {
                    Throwable e = wrappedExc.getCause();
                    System.out.println(m + " failed: " + e);
                } catch (Exception e) {
                    System.out.println("Invalid @Test: " + m);
                }
            }

            if (m.isAnnotationPresent(ExceptionTest.class)) {
                tests++;
                try {
                    m.invoke(null);
                    System.out.printf("Test %s failed: no exception%n", m);
                } catch (InvocationTargetException wrappedExc) {
                    Throwable e = wrappedExc.getCause();
                    int oldPassed = passed;
                    Class<? extends Throwable>[] expectedTypes = m.getAnnotation(ExceptionTest.class).value();
                    for (Class<?> type : expectedTypes) {
                        if (type.isInstance(e)) {
                            passed++;
                            break;
                        }
                    }
                    if (passed == oldPassed) {
                        System.out.printf("Test %s failed: expected %s, got %s%n", m,
                                Stream.of(expectedTypes).map(t -> t.getName()).collect(Collectors.joining(",")), e);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid @Test: " + m);
                }
            }

            if (m.isAnnotationPresent(RepeatableExceptionTest.class)
                    || m.isAnnotationPresent(RepeatableExceptionTestContainer.class)) {
                tests++;
                try {
                    m.invoke(null);
                    System.out.printf("Test %s failed: no exception%n", m);
                } catch (InvocationTargetException wrappedExc) {
                    Throwable e = wrappedExc.getCause();
                    int oldPassed = passed;
                    RepeatableExceptionTest[] testAnnotations = m.getAnnotationsByType(RepeatableExceptionTest.class);
                    for (RepeatableExceptionTest testAnnotation : testAnnotations) {
                        if (testAnnotation.value().isInstance(e)) {
                            passed++;
                            break;
                        }
                    }
                    if (passed == oldPassed) {
                        System.out.printf("Test %s failed: expected %s, got %s%n", m,
                                Stream.of(testAnnotations).map(t -> t.value().getName())
                                        .collect(Collectors.joining(",")),
                                e);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid @Test: " + m);
                }
            }
        }
        System.out.printf("Passed: %d, Failed: %d%n", passed, tests - passed);
    }
}

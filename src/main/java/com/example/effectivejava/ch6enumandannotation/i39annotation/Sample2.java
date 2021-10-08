package com.example.effectivejava.ch6enumandannotation.i39annotation;

import java.util.ArrayList;
import java.util.List;

public class Sample2 {
    @ExceptionTest(ArithmeticException.class)
    public static void m1() {
        // Test should pass
        int i = 0;
        i = i / i;
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m2() {
        // Should fail (wrong exception)
        int[] a = new int[0];
        int i = a[1];
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m3() {
        // Should fail (no exception)
    }

    @ExceptionTest({ IndexOutOfBoundsException.class, NullPointerException.class })
    public static void m4() {
        List<String> list = new ArrayList<>();

        // The spec permits this method to throw either
        // IndexOutOfBoundsException or NullPointerException
        list.addAll(5, null);
    }

    @RepeatableExceptionTest(IndexOutOfBoundsException.class)
    public static void m5() {
        // Should fail (no exception)
    }

    @RepeatableExceptionTest(IndexOutOfBoundsException.class)
    @RepeatableExceptionTest(NullPointerException.class)
    public static void m6() {
        List<String> list = new ArrayList<>();

        // The spec permits this method to throw either
        // IndexOutOfBoundsException or NullPointerException
        list.addAll(5, null);
    }

    @RepeatableExceptionTestContainer({
            @RepeatableExceptionTest(IndexOutOfBoundsException.class),
            @RepeatableExceptionTest(NullPointerException.class) })
    public static void m7() {
        List<String> list = new ArrayList<>();

        // The spec permits this method to throw either
        // IndexOutOfBoundsException or NullPointerException
        list.addAll(5, null);
    }
}

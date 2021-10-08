package com.example.effectivejava.ch6enumandannotation.i39annotation;

public class Sample {

    @Test
    public static void m1() {}

    // Test should pass
    public static void m2() {}

    @Test
    public static void m3() {
        // Test should fail
        throw new RuntimeException("Boom");
    }

    public static void m4() {}

    @Test
    public void m5() {} // INVALID USE: nonstatic method（注解注释说明了该注解只适用于无参静态方法）

    public static void m6() {}

    @Test
    public static void m7() {
        // Test should fail
        throw new RuntimeException("Carsh");
    }

    public static void m8() {}
}

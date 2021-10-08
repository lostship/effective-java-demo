package com.example.effectivejava.ch2instantiate.i4noninstantiability;

public class UtilityClass {
    // 不可实例化
    private UtilityClass() {
        // 防止内部误调用构造方法
        throw new AssertionError();
    }

    public static void method() {};
}

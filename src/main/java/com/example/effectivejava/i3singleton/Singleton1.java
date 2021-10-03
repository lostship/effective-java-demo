package com.example.effectivejava.i3singleton;

import java.io.Serializable;

public class Singleton1 implements Serializable {
    public static final Singleton1 INSTANCE = new Singleton1();

    private Singleton1() {
        // 抵御反射攻击
        if (INSTANCE != null) {
            throw new AssertionError();
        }
    }

    /**
     * 防止反序列化创建新对象
     */
    private Object readResolve() {
        return INSTANCE;
    }

    public void method() {}
}

package com.example.effectivejava.ch2instantiate.i3singleton;

import java.io.Serializable;

public class Singleton2 implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Singleton2 INSTANCE = new Singleton2();

    private Singleton2() {
        // 抵御反射攻击
        if (INSTANCE != null) {
            throw new AssertionError();
        }
    }

    public static Singleton2 getInstance() {
        return INSTANCE;
    }

    /**
     * 防止反序列化创建新对象
     */
    private Object readResolve() {
        return INSTANCE;
    }

    public void method() {}
}

package com.example.effectivejava.ch2instantiate.i3singleton;

/**
 * 单元素的枚举类型，通常是实现单例模式的最佳方法
 *
 * 如果单例必须扩展一个超类，而不是Enum时，则不适合用这种方法
 */
public enum Singleton3 {
    INSTANCE;

    public void method() {}
}

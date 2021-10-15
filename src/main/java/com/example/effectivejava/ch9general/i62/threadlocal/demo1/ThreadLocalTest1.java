package com.example.effectivejava.ch9general.i62.threadlocal.demo1;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 不好的ThreadLocal实现
 * 
 * Thread在java1.0版本中就存在，而ThreadLocal是java1.2版本增加的。
 * 因为是增加管理线程局部变量的功能，最初会想到修改java.lang.Thread的实现，新增一个私有map，
 * 将线程局部变量存放在线程私有map中，对外增加accessor、mutator方法。
 * 
 * 这样看起来实现简单，但是有很多问题：
 * 1.违反了开闭原则。
 * 2.Thread是可扩展的，最初设计中并没有公开管理线程局部变量的API，如果已有其它程序扩展了Thread，
 * 可能遇到方法被覆盖等由于继承带来的问题（i18组合优先于继承），导致无法正常使用新增的局部变量功能。
 * 
 * 因此不应该直接修改Thread的公开API，应该单独实现ThreadLocal类，维护Thread和线程局部变量的关系。
 */
public class ThreadLocalTest1 {
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(() -> {
                setThreadLocalData();
                useThreadLocalData();
            });
            t.start();
        }
    }

    private static void setThreadLocalData() {
        int i = new Random().nextInt();
        ((ThreadDemo) Thread.currentThread()).setLocal(KEY_ID, i);
        ((ThreadDemo) Thread.currentThread()).setLocal(KEY_NAME, "name" + i);
    }

    private static void useThreadLocalData() {
        Integer id = (Integer) ((ThreadDemo) Thread.currentThread()).getLocal(KEY_ID);
        String name = (String) ((ThreadDemo) Thread.currentThread()).getLocal(KEY_NAME);
        System.out.println(id + " " + name);
    }
}

/**
 * 假设这是java.lang.Thread实现线程局部变量的方式
 * 
 * @since 1.2
 */
class ThreadDemo extends java.lang.Thread {
    private final Map<String, Object> localMap = new HashMap<>();

    public ThreadDemo(Runnable runnable) {
        super(runnable);
    }

    public void setLocal(String key, Object value) {
        localMap.put(key, value);
    }

    public Object getLocal(String key) {
        return localMap.get(key);
    }

    public void removeLocal(String key) {
        localMap.remove(key);
    }
}

/**
 * 假设在有线程局部变量之前，已经有程序扩展了Thread
 * 
 * @since 1.0
 */
class CustomThread extends Thread {
    public CustomThread(Runnable runnable) {
        super(runnable);
    }

    /**
     * 直接修改Thread的公开API会给继承带来问题
     */
    public String getLocal(String key) {
        return this.getThreadGroup().getName();
    }
}

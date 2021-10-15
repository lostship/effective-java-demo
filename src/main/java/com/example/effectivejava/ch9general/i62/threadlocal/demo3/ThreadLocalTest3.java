package com.example.effectivejava.ch9general.i62.threadlocal.demo3;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * TODO WeakReference，内存泄漏等问题
 */
public class ThreadLocalTest3 {
    public static final ThreadLocalDemo ids = new ThreadLocalDemo();
    public static final ThreadLocalDemo names = new ThreadLocalDemo();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            Thread t = new ThreadDemo(() -> {
                setThreadLocalData();
                useThreadLocalData();
            });
            t.start();
        }
    }

    private static void setThreadLocalData() {
        int i = new Random().nextInt();
        ids.set(i);
        names.set("name" + i);
    }

    private static void useThreadLocalData() {
        Integer id = (Integer) ids.get();
        String name = (String) names.get();
        System.out.println(id + " " + name);
    }
}

class ThreadDemo extends java.lang.Thread {
    Map<ThreadLocalDemo, Object> threadLocals = new HashMap<>();

    public ThreadDemo(Runnable runnable) {
        super(runnable);
    }
}

class ThreadLocalDemo {
    public ThreadLocalDemo() {}

    private Map<ThreadLocalDemo, Object> getThreadLocals() {
        return ((ThreadDemo) Thread.currentThread()).threadLocals;
    }

    public void set(Object value) {
        getThreadLocals().put(this, value);
    }

    public Object get() {
        return getThreadLocals().get(this);
    }

    public void remove() {
        getThreadLocals().remove(this);
    }
}

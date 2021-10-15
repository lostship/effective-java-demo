package com.example.effectivejava.ch9general.i62.threadlocal;

import java.lang.reflect.Field;
import java.util.Optional;

public class ThreadLocalTest {
    public static void main(String[] args) throws Exception {
        Class<Thread> threadClass = Thread.class;
        Field threadLocalsField = threadClass.getDeclaredField("threadLocals");
        threadLocalsField.setAccessible(true);

        Class<?> threadLocalMapClass = threadLocalMapClass();
        Field tableField = threadLocalMapClass.getDeclaredField("table");
        tableField.setAccessible(true);

        Class<?> entryClass = entryClass(threadLocalMapClass);
        Field valueField = entryClass.getDeclaredField("value");
        valueField.setAccessible(true);

        Class<?> referenceClass = entryClass.getSuperclass().getSuperclass();
        Field referentField = referenceClass.getDeclaredField("referent");
        referentField.setAccessible(true);

        Thread t = new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    setLocalData();
                    printEntries(threadLocalsField, tableField, referentField, valueField);

                    triggerYGC();
                    printEntries(threadLocalsField, tableField, referentField, valueField);

                    Thread.sleep(50);
                }

                setLocalData();
                printEntries(threadLocalsField, tableField, referentField, valueField);

                for (int i = 0; i < 10; i++) {
                    removeLocalData();
                    printEntries(threadLocalsField, tableField, referentField, valueField);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        t.start();
        t.join();
    }

    private static void setLocalData() throws Exception {
        ThreadLocal<Object> tl = new ThreadLocal<>();
        tl.set(1);
        System.out.println("set local key " + tl);
        System.out.println("========");
    }

    private static void removeLocalData() throws Exception {
        ThreadLocal<Object> tl = new ThreadLocal<>();
        tl.set(1);
        tl.remove();
        System.out.println("call remove");
        System.out.println("========");
    }

    private static void triggerYGC() {
        @SuppressWarnings("unused")
        int[] unused = new int[2000000]; // -Xms10m -Xmx10m
        System.out.println("gc");
    }

    private static void printEntries(Field threadLocalsField, Field tableField, Field referentField, Field valueField)
            throws Exception {
        Object map = threadLocalsField.get(Thread.currentThread());
        Object[] entries = (Object[]) tableField.get(map);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object e : entries) {
            if (e != null) {
                String key = Optional.ofNullable(referentField.get(e)).map(Object::toString).orElse(null);
                if (key != null) {
                    key = key.substring(key.indexOf("@") + 1);
                }
                String value = Optional.ofNullable(valueField.get(e)).map(Object::toString).orElse(null);
                sb.append(key).append(":").append(value).append(", ");
            }
        }
        sb.append("]");
        System.out.println(sb.toString().replaceFirst(", ]", "]"));
        System.out.println("========");
    }

    private static Class<?> threadLocalMapClass() throws Exception {
        @SuppressWarnings("rawtypes")
        Class<ThreadLocal> cl = ThreadLocal.class;
        for (Class<?> c : cl.getDeclaredClasses()) {
            if (c.getName().endsWith("ThreadLocalMap")) {
                return c;
            }
        }
        throw new NullPointerException();
    }

    private static Class<?> entryClass(Class<?> threadLocalMapClass) throws Exception {
        for (Class<?> c : threadLocalMapClass.getDeclaredClasses()) {
            if (c.getName().endsWith("Entry")) {
                return c;
            }
        }
        throw new NullPointerException();
    }
}

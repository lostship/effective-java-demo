package com.example.effectivejava.ch9general.i62.threadlocal.demo2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 不好的ThreadLocal实现
 * 
 * 实现ThreadLocal，关联Thread和线程局部变量，最初会想到使用线程实例自身作为key，将局部变量存放在ThreadLocal的私有map域中。
 * 因为一个线程可能会有多个线程局部变量，Thread作为key在一个map中只能映射到一个变量，所以每个变量使用一个ThreadLocal实例即可。
 * 
 * 使用Thread本身作为key，如果忘记调用remove，并且ThreadLocal对象长期存活的话，
 * 就会一直持有Thread实例的引用，导致Thread实例无法释放，
 * 而且无法再调用remove方法移除这个线程的键值对，造成内存泄漏。
 * 并且随着程序运行，可能创建大量线程，最终会造成内存溢出。
 * 
 * 此外随着线程数量和线程局部变量增多，维护一个共享的map查询效率会降低。
 * 因此最好每个Thread维护自己的包级私有map域，ThreadLocal和Thread在同一个包中，可以访问Thread的map域。
 * 并且这样随着Thread实例销毁，map中的数据也会被释放。
 * 
 * 根据之前的演变，目的是通过ThreadLocal，维护每一个Thread和它的多个局部变量的关系，
 * 而每一个映射，只能维护一个Thread和它的一个局部变量的关联，因此需要多个ThreadLocal实例关联不同的局部变量。
 * 即对于每一个Thread实例而言，局部变量和ThreadLocal实例是一对一关系。
 * 因此ThreadLocal实例更适合作为key（unique、unforgeable）。
 * （不可伪造的key：保证每个线程创建的key对象在map中不相等，String就是一个反例，不同线程可能传递了相同的字符串作为key，造成冲突）
 */
public class ThreadLocalTest2 {
    public static final ThreadLocalDemo ids = new ThreadLocalDemo();
    public static final ThreadLocalDemo names = new ThreadLocalDemo();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(() -> {
                setThreadLocalData();
                useThreadLocalData();
            });
            t.start();
        }

        Thread.sleep(1000);
        System.out.println(ids.keySet());
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

class ThreadLocalDemo {
    private final Map<Thread, Object> map = new HashMap<>();

    public ThreadLocalDemo() {}

    public void set(Object value) {
        map.put(Thread.currentThread(), value);
    }

    public Object get() {
        return map.get(Thread.currentThread());
    }

    public void remove() {
        map.remove(Thread.currentThread());
    }

    public Set<Thread> keySet() {
        return map.keySet();
    }
}

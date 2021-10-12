package com.example.effectivejava.ch11concurrency.i79avoidexcessivesync;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.effectivejava.ch11concurrency.i79avoidexcessivesync.collection.ForwardingSet;

/**
 * 在类的同步区域中调用外来方法（alien）可能导致的问题示例
 */
public class BadObservableSet<E> extends ForwardingSet<E> {

    private final Set<E> s;
    private final List<SetObserver<E>> obs = new ArrayList<>();

    public static <E> BadObservableSet<E> of(Set<E> s) {
        return new BadObservableSet<>(s);
    }

    private BadObservableSet(Set<E> s) {
        this.s = s;
    }

    @Override
    protected Set<E> decorated() {
        return s;
    }

    public boolean addObserver(SetObserver<E> ob) {
        synchronized (obs) {
            return obs.add(ob);
        }
    }

    public boolean removeObserver(SetObserver<E> ob) {
        synchronized (obs) {
            return obs.remove(ob);
        }
    }

    private void notifyElementAdded(E e) {
        synchronized (obs) {
            for (SetObserver<E> ob : obs) {
                ob.added(this, e);
            }
        }
    }

    @FunctionalInterface
    public interface SetObserver<E> {

        void added(BadObservableSet<E> set, E element);

    }

    @Override
    public boolean add(E e) {
        boolean added = super.add(e);
        if (added) {
            notifyElementAdded(e);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E e : c) {
            result |= add(e);
        }
        return result;
    }

    public static void main(String[] args) {
        test2();
    }

    private static void test1() {
        BadObservableSet<Integer> set = BadObservableSet.of(new HashSet<>());

        set.addObserver(new SetObserver<>() {
            /**
             * 遍历观察者列表过程中修改列表，抛出异常
             */
            @Override
            public void added(BadObservableSet<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 5) {
                    set.removeObserver(this);
                }
            }
        });

        for (int i = 0; i < 10; i++) {
            set.add(i);
        }
    }

    private static void test2() {
        BadObservableSet<Integer> set = BadObservableSet.of(new HashSet<>());

        set.addObserver(new SetObserver<>() {
            /**
             * 产生死锁示例
             * 从同步区域中调用外来方法，设计不当容易造成死锁
             * 
             * 此外同步区域所保护的数据应该处于一致的状态。
             * 但是在同步区域代码执行过程中，数据可能处于不一致的状态。
             * 这时如果在同步区域调用一个外来方法，如果是如同第一个例子所示的情况，调用线程已经获得了锁，由于java中的锁是可重入的，因此该线程试图再次获得该锁时会成功。
             * 这时外来方法就能够在数据处于不一致状态下，调用其它同步方法对数据进行修改，
             * 而其它方法可能是建立在数据一致性的约束条件下的，因此可能破坏同步区域保护的数据的最终一致性。
             */
            @Override
            public void added(BadObservableSet<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 5) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(() -> set.removeObserver(this)).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });

        for (int i = 0; i < 10; i++) {
            set.add(i);
        }
    }

}

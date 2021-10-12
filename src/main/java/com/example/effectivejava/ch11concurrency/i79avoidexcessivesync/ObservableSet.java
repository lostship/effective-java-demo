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
 * <p>
 * 为了避免liveness failure和safety failure，在一个被同步的方法或者代码块中，永远不要放弃对客户端的控制。
 * 换句话说，在一个被同步的区域内部，不要调用设计成要被覆盖的方法，或者是由客户端以函数对象的形式提供的方法。
 * 
 * <p>
 * 从包含该同步区域的类的角度来看，这样的方法是外来的（alien），这个类不知道方法会做什么事情，也无法控制它。
 * 根据外来方法的作用，从同步区域中调用它可能导致异常、死锁或者数据破坏。
 * 
 * <p>
 * 在同步区域之外被调用的外来方法被称作开放调用（open call），除了可以避免失败，还可以极大地增加并发性。
 * 外来方法可能运行任意时长，如果在同步区域调用外来方法，其它线程对受保护资源的访问就会遭到不必要的拒绝。
 * 
 * <p>
 * 通常来说，应该在同步区域内做尽可能少的工作。
 * 
 * <p>
 * 在能够满足程序正确性的前提下，尽量避免<strong>过度</strong>同步，
 * 因为这会失去并行的机会，以及因为需要确保每个CPU内核都有一致的内存视图而导致的延迟，还会潜在限制虚拟机的代码优化执行能力。
 * 
 * <p>
 * 编写一个可变的类，有两种方式处理同步：
 * 1.省略所有同步，如果想要并发使用，就允许客户端在必要时从外部同步。
 * 2.通过内部同步，使这个类变成线程安全的，适当情况有机会通过设计获得比从外部锁定整个对象更高的并发性。
 * 在设计可变类时，应该考虑它是否需要自己实现同步，当不确定的时候，就不要使用内部同步，而应该建立文档注明它不是线程安全的。这比不要过度同步更重要。
 */
public class ObservableSet<E> extends ForwardingSet<E> {

    private final Set<E> s;
    private final List<SetObserver<E>> obs = new ArrayList<>();

    public static <E> ObservableSet<E> of(Set<E> s) {
        return new ObservableSet<>(s);
    }

    private ObservableSet(Set<E> s) {
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
        // 安全性拷贝，防止遍历过程中原始列表被修改造成错误
        List<SetObserver<E>> snapshot = null;
        synchronized (obs) {
            snapshot = new ArrayList<>(obs);
        }

        // 外部方法的调用移到同步区域之外，防止死锁
        // 这样没有锁也可以安全地遍历列表了，并且可以避免异常和死锁
        for (SetObserver<E> ob : snapshot) {
            ob.added(this, e);
        }
    }

    @FunctionalInterface
    public interface SetObserver<E> {

        void added(ObservableSet<E> set, E element);

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
        ObservableSet<Integer> set = ObservableSet.of(new HashSet<>());

        set.addObserver(new SetObserver<>() {
            @Override
            public void added(ObservableSet<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 20) {
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

        for (int i = 0; i < 100; i++) {
            set.add(i);
        }
    }

}

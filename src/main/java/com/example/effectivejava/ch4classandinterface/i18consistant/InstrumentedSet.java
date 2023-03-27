package com.example.effectivejava.ch4classandinterface.i18consistant;

import java.util.Collection;
import java.util.Set;

/**
 * 在包的内部使用继承非常安全，一切都在包的维护人员控制下。
 * 超出包边界的继承，则非常危险，在父类实现进行修改时，以及子类依赖父类实现细节重写一些方法时，很容易出现错误。
 * 
 * 组合优于继承的思想这时就非常有效
 * 
 * InstrumentHashSet类称为包装类，把另一个Set实例包装了起来，
 * 这正是修饰者（Decorator）模式的实现，因为它对Set进行了修饰，增加了计数特性。
 * 
 * 如果直接继承HashSet，覆盖add和addAll方法，因为super.addAll实际上调用add方法，这种self-use自用性，造成counter累加的结果就会出错。
 * 绝对不要在可被覆盖的方法中使用self-used，如果必须使用继承，把可被覆盖方法共用的代码封装成私有方法。
 * 
 * ForwardingSet是可重用的转发类，其中的方法称为转发方法，使得它的子类不需要依赖于Set中的实现细节。
 * 即便Set中方法的实现细节发生变化，对InstrumentHashSet完全无感知无影响。
 * 更好的实现可以参考guava中的ForwardingXXX类。
 */
public final class InstrumentedSet<E> extends ForwardingSet<E> {
    private final Set<E> set;
    private int addCount = 0;

    public InstrumentedSet(Set<E> set) {
        this.set = set;
    }

    @Override
    protected Set<E> delegate() {
        return set;
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}

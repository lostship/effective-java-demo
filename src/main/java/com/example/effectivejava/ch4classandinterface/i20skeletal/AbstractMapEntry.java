package com.example.effectivejava.ch4classandinterface.i20skeletal;

import java.util.Map.Entry;
import java.util.Objects;

/**
 * 骨架实现（Skeletal implementation） & 抽象的骨架实现类
 * 
 * 骨架实现是在接口、抽象类中提供缺省的实现，方便后续子类实现和扩展，子类只需要实现最基本的接口方法即可。
 * 这就是模板方法（Template Method）模式。
 * 
 * 编写骨架实现类：
 * 1.先分析接口，确定哪些方法是基本方法（必须要子类实现），其他方法可以基于基本方法来实现（可以提供缺省方法）。
 * 2.在接口中通过default方法，为可实现的方法提供缺省实现，如果基本方法和缺省方法覆盖了接口，任务就完成了。
 * 3.但是接口default方法的协助能力有限（不能包含实例域和非公有静态域，也不能覆盖Object的方法）
 * 如果没有全部覆盖，就可以编写一个抽象的骨架实现类，声明实现接口，实现剩下的缺省接口方法。
 * 
 * Java集合框架重要的接口都提供了抽象的骨架实现类：AbstractCollection、AbstractList等。
 * 本例的AbstractMapEntry可以参考guava中的实现。
 * 
 * i20还提到了模拟多重继承（simulated multiple inheritance），
 * 实现了接口的类，可以把对于接口方法的调用，转发到一个私有内部类的实例上，这个私有内部类扩展了骨架实现类。
 * 这项技术具有多重继承的大部分有点，同时又避免了相应的缺陷。
 * （具体优缺点没有详细阐述）
 * 
 * 骨架实现是为了继承的目的而设计的，因此应该遵循相应设计和文档上的指导原则。
 * 
 * 骨架实现上有个小小的不同，就是简单实现（simple implementation）。
 * AbstractMap.SimpleEntry就是个例子，它就像骨架实现一样，实现了接口，且为了继承而设计。
 * 不同之处在于它不是抽象类，它是最简单的可能的一种有效实现。可以直接使用，也可以视需要扩展。
 */
public abstract class AbstractMapEntry<K, V> implements Entry<K, V> {
    /**
     * Entries in a modifiable map must override this method
     */
    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entry) {
            Entry<?, ?> that = (Entry<?, ?>) obj;
            return Objects.equals(this.getKey(), that.getKey())
                    && Objects.equals(this.getValue(), that.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        K k = getKey();
        V v = getValue();
        return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
    }

    @Override
    public String toString() {
        return getKey() + "=" + getValue();
    }
}

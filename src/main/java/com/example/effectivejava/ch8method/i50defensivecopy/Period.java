package com.example.effectivejava.ch8method.i50defensivecopy;

import java.util.Date;

/**
 * 必要时使用保护性拷贝
 * 
 * 如果没有对象的帮助，另一个类不可能修改对象的内部状态，但是对象很容易在无意识的情况下提供这种帮助。
 * 为保护实例内部状态不受攻击，必要时需要对构造器每个可变参数进行保护性拷贝（defensive copy）。
 * 
 * 如果域是包级私有的，确信调用者不会修改，也可以不进行保护性拷贝，
 * 但是在文档中必须说明调用者不能修改受影响的参数或返回值。
 * 
 * 如果是跨越包的作用范围，也不一定总适合在将可变参数整合到对象中前进行保护性拷贝。
 * 这是就需要文档说明客户端的职责，需要客户端承诺交接（hand off）参数对象的控制权。
 * 或者即便客户端破坏约束条件，也不会影响到到除了该客户端之外的其他对象
 * （作为Map的键的对象，以及包装类等就是这种情况，客户单可以破坏约束条件，但是这样只会伤害到客户端自己）。
 */
public class Period {
    // Date类型是可变的，该类型已经过时，应该替换为Instant、LocalDateTime等。
    // java8之前的版本一般保存Date.getTime()返回的long基本类型，而不是Date对象引用。
    private final Date start;
    private final Date end;

    public Period(Date start, Date end) {
        // 1.Date是可变的，其它代码不通过Period提供的方法就可以改变start和end的值，从而改变Period的内部状态，因此要对它们进行保护性拷贝。
        // 2.注意这里先进行保护性拷贝，再对拷贝之后的对象进行参数的有效性检查，
        // 避免原始对象在参数检查到拷贝期间被其它线程改变（TOCTOU攻击，Time-Of-Check/Time-Of-Use）。
        // 3.Date不是final的，clone方法不一定返回Date类，也有可能是恶意设计的子类。
        // 对于参数类型可以被不可信任方子类化的参数，不要使用clone方法进行保护性拷贝。
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());

        if (this.start.compareTo(this.end) > 0) {
            throw new IllegalArgumentException(start + " after " + end);
        }
    }

    public Date start() {
        // accessor返回可变内部域的保护性拷贝
        return new Date(start.getTime());
    }

    public Date end() {
        // accessor与构造器不同，可以使用非final类的clone方法，因为我们确定自己类内部成员的类型，不可能是不可信任的子类
        // 但是一般情况下，最好使用构造器或者静态工厂
        return (Date) end.clone();
    }
}

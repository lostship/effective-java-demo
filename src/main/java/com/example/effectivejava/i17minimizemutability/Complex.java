package com.example.effectivejava.i17minimizemutability;

/**
 * 不可变类示例：复数类
 * 
 * 
 * 优点：见示例中注释
 * 
 * 
 * 缺点：
 * 不可变类的缺点是，对于每个不同的值都需要一个单独的对象，而有时创建新对象的代价可能非常高，特别是大型的对象。
 * 比如有一个上百万位的BigInteger，想要改变它的低位：
 * BigInteger moby = ...;
 * moby = moby.flipBit(0);
 * flipBit方法创建了一个新的实例，也有上百万位长，其实和原来的数值只有一位不同，却消耗了非常多的时间和空间。
 * 如果执行一个多步骤的操作，每个步骤都会产生一个新对象，除了最后的结果外，其他的对象最终都会被丢弃，这样就会造成性能问题。
 * 
 * 应对这个缺点有两个办法：
 * 1.如果可以预测客户端可能在不可变类上执行哪些多步骤的复杂操作，可以提供一个包级私有的可变的配套类（companing class）。
 * 例如BigInteger就有这样的配套类，用于加速诸如模指数等运算。
 * 2.如果无法预测，可以提供一个公有的可变配套类，比如String的可变配套类是StringBuilder。
 * 
 * 
 * 更多详见i17
 */
/**
 * 不可变类不可以被子类化，防止子类破坏不可变行为
 */
public final class Complex {
    /**
     * 不可变对象本质上是线程安全的，它们不要求同步，可以被自由地共享。
     * 应该充分利用这种优势，鼓励客户端尽可能重用实例。
     * 比如对于频繁用到的值，可以为它们提供公有静态常量。
     * 
     * 不仅可以共享不可变对象本身，甚至可以共享它们的内部成员。
     * 比如BigInteger内部使用一个int表示符号，一个int[]表示数值。
     * negate方法产生一个新的BigInteger，其中数值相同，符号相反。
     * 它并不需要拷贝数组，新的对象也指向原来的数组，因为这个数组是不会被改变的。
     */
    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex I = new Complex(0, 1);

    /**
     * 这种实例重用思想可以被进一步扩展。
     * 不可变的类可以提供一些静态工厂方法，把被频繁请求的实例缓存起来。
     * 从而降低内存占用和GC成本
     * 
     * 所有基本类型的包装类和BigInteger都有这样的静态工厂。
     * 公有构造器无法使用缓存。
     * 在设计新类的时候，使用静态工厂代替公有构造器，可以让以后有添加缓存的灵活性，而不必影响客户端。
     */
    public static Complex valueOf(double re, double im) {
        // TODO 从缓存获取
        return new Complex(re, im);
    }

    private final double re;
    private final double im;

    /**
     * 除了通过使用final禁止被扩展外，
     * 也可以将构造器声明为私有的或包级私有的，并提供公有的静态工厂方法。
     * 这样最灵活，因为可以使用多个包级私有的实现类，还可以通过改善静态工厂的缓存能力改进性能。
     */
    private Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart() {
        return re;
    }

    public double imaginaryPart() {
        return im;
    }

    /**
     * 注意这里返回了一个新的Complex对象，没有改变原有的对象。
     * 
     * 这样的方法成为函数的（functional）方法，方法返回一个函数的结果，这些函数对操作数进行运算但并不修改它。
     * 与之相对的更常见的是过程的（procedural）或者命令式的（imperative）方法，将一个过程作用在操作数上，会导致它的状态发生改变。
     * 
     * 注意这里方法的名称是介词（plus），而不是动词（add），这样是在强调方法不会改变对象的值。
     */
    public Complex plus(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    public Complex minus(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    public Complex times(Complex c) {
        return new Complex(re * c.re - im * c.im,
                re * c.im + im * c.re);
    }

    public Complex dividedBy(Complex c) {
        double tmp = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / tmp,
                (im * c.re - re * c.im) / tmp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Complex)) {
            return false;
        }

        Complex c = (Complex) obj;
        return Double.compare(re, c.re) == 0
                && Double.compare(im, c.im) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(re) + Double.hashCode(im);
    }

    @Override
    public String toString() {
        return "(" + re + " + " + im + "i)";
    }

    /**
     * 不可变对象不需要clone，因为即便克隆出来一个新对象，也是和原始对象永远相等的。
     */
    @Deprecated
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}

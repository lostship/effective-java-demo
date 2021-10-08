package com.example.effectivejava.ch3commonmethod.i10equals;

import java.util.Objects;

/**
 * equals方法通用约定：
 * 自反性
 * 对称性
 * 传递性
 * 一致性
 * 非空性
 */
public class PhoneNumber {
    private final short areaCode, prefix, lineNum;

    public PhoneNumber(short areaCode, short prefix, short lineNum) {
        this.areaCode = rangeCheck(areaCode, 999, "area code");
        this.prefix = rangeCheck(prefix, 999, "prefix");
        this.lineNum = rangeCheck(lineNum, 9999, "line num");
    }

    private static short rangeCheck(int val, int max, String arg) {
        if (val < 0 || val > max) {
            throw new IllegalArgumentException(arg + ": " + val);
        }
        return (short) val;
    }

    /**
     * 编写好的equals方法步骤：
     * 1.先使用“==”比较
     * 2.使用instanceof检查类型
     * 3.转换为正确的类型
     * 4.比较关键域
     * 
     * 编写完成后要检查是否满足：对称性、传递性、一致性
     * 
     * 最后需要记住，重写了equals方法，就一定要重写hashcode方法
     * 不要企图让equals方法过于智能，能够兼容处理一切别名形式
     * 除非必要，不要轻易重写equals方法
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PhoneNumber)) {
            return false;
        }

        PhoneNumber pn = (PhoneNumber) obj;
        return pn.areaCode == areaCode
                && pn.prefix == prefix
                && pn.lineNum == lineNum;
    }

    /**
     * hashCode要与equals方法的结果一致
     */
    @Override
    public int hashCode() {
        return Objects.hash(areaCode, prefix, lineNum);
    }

    /**
     * 除了静态工具类、枚举类型，建议在所有可实例化的类中重写toString方法，
     * 以美观的形式返回一个对象简介、有用的描述，除非超类已经提供了通用的toString方法。
     * 
     * Returns the string representation of this phone number.
     * The string consists of twelve characters whose format is
     * "XXX-YYY-ZZZZ", where XXX is the area code, YYY is the
     * prefix, and ZZZZ is the line number. Each of the capital
     * letters reprensents a single decimal digit.
     * 
     * If any of the three parts of this phone number is to small
     * to fill up its field, the field is padded with leading zeros.
     * For example, if the value of the line number is 123, the last
     * four characters of the string representation will be "0123".
     */
    @Override
    public String toString() {
        return String.format("%03d-%03d-%04d", areaCode, prefix, lineNum);
    }
}

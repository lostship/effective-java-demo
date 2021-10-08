package com.example.effectivejava;

import java.math.BigInteger;
import java.util.stream.LongStream;

public class Test {
    public static void main(String[] args) {
        System.out.println(pi((long) Math.pow(10, 5)));
    }

    /**
     * Prime-counting stream pipeline - benefits form parallelization
     */
    static long pi(long n) {
        return LongStream.rangeClosed(2, n)
                .parallel()
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }
}

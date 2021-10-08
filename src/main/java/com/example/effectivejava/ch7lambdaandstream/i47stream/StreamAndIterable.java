package com.example.effectivejava.ch7lambdaandstream.i47stream;

import java.math.BigInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamAndIterable {
    public static void main(String[] args) {
        primes().map(p -> BigInteger.TWO.pow(p.intValueExact()).subtract(BigInteger.ONE))
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .limit(10)
                .forEach(mp -> System.out.println(mp.bitLength() + ": " + mp));

        for (BigInteger bi : iterableOf(primes())) {
            System.out.println(bi);
        }
    }

    static Stream<BigInteger> primes() {
        return Stream.iterate(BigInteger.TWO, BigInteger::nextProbablePrime);
    }

    /**
     * Adapter from Stream<E> to Iterable<E>
     */
    static <E> Iterable<E> iterableOf(Stream<E> stream) {
        return stream::iterator;
    }

    /**
     * Adapter from Iterable<E> to Stream<E>
     */
    static <E> Stream<E> streamOf(Iterable<E> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}

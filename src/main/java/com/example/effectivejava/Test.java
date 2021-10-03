package com.example.effectivejava;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.collection.SynchronizedCollection;

public class Test {
    public static void main(String[] args) {
        Collection<Integer> c = new ArrayList<>();
        SynchronizedCollection sc = (SynchronizedCollection) CollectionUtils.synchronizedCollection(c);
        sc.removeIf(i -> false);
    }
}

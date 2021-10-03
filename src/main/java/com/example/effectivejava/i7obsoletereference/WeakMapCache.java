package com.example.effectivejava.i7obsoletereference;

import java.util.Map;
import java.util.WeakHashMap;

public class WeakMapCache {
    private static final Map<String, Object> cache = new WeakHashMap<>();

    public static void put(String k, Object v) {
        cache.put(k, v);
    }

    public static Object get(String k) {
        return cache.get(k);
    }
}

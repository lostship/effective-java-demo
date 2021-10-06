package com.example.effectivejava.i33heterogeneouscontainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 类型安全的异构容器（typesafe heterogeneous container）
 */
public class Favorites {
    private final Map<Class<?>, Object> favorites = new HashMap<>();

    public <T> void put(Class<T> type, T instance) {
        favorites.put(Objects.requireNonNull(type),
                type.cast(instance)); // Archieving runtime type safety with a dynamic cast.
                                      // 很多集合包装类（CheckedSet\CheckedList\CheckedMap）使用了这种技巧。
                                      // 使用这些包装类在混有泛型和原始类型的代码中追溯“是谁把错误元素添加到集合中”很有帮助。
    }

    public <T> T get(Class<T> type) {
        return type.cast(favorites.get(type));
    }

    public static void main(String[] args) {
        Favorites f = new Favorites();
        f.put(String.class, "hello");
        f.put(Integer.class, 1);
        f.put(Class.class, Favorites.class);

        System.out.println(f.get(String.class));
        System.out.println(f.get(Integer.class));
        System.out.println(f.get(Class.class));
    }
}

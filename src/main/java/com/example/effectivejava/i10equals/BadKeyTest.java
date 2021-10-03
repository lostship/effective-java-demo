package com.example.effectivejava.i10equals;

import java.util.HashMap;
import java.util.Map;

public class BadKeyTest {
    public static void main(String[] args) {
        Map<BadKey, Object> map = new HashMap<>();
        BadKey key = new BadKey(1);
        map.put(key, 1);
        System.out.println(map.get(key));

        // map在链表查询时，不仅会使用equals比较，还会优先使用缓存的hashCode比较
        // 如果键对象hashCode变化就查询不到了
        // 因此如果对象作为键，一定要确保hashCode不会变化
        key.setHashCode(2);
        System.out.println(map.get(key));
    }
}

class BadKey {
    private int hashCode;

    public BadKey(int hashCode) {
        this.hashCode = hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}

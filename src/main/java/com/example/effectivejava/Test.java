package com.example.effectivejava;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        String a = "1";

        String b = "" + 1;

        int n = 1;
        String c = "" + n;

        final int m = 1;
        String d = "" + m;

        String e = Integer.toString(1);

        String f = new String("1");

        System.out.println(a == b);
        System.out.println(a == c);
        System.out.println(a == d);
        System.out.println(a == e);
        System.out.println(a == f);

        Set<?> set = new HashSet<>();
        Set<String> s1 = (Set<String>) set;
        s1.add("1");
        Set<Integer> s2 = (Set<Integer>) set;
        s2.add(1);
    }
}

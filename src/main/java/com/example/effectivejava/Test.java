package com.example.effectivejava;

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
    }
}

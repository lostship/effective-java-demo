package com.example.effectivejava.i10equals.symmetry.good;

import java.util.Objects;

public class CaseInsensitiveString implements Comparable<CaseInsensitiveString> {
    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CaseInsensitiveString
                && s.equalsIgnoreCase(((CaseInsensitiveString) obj).s);
    }

    @Override
    public int compareTo(CaseInsensitiveString o) {
        return String.CASE_INSENSITIVE_ORDER.compare(s, o.s);
    }

    public static void main(String[] args) {
        CaseInsensitiveString cis = new CaseInsensitiveString("ABC");
        String s = "abc";
        System.out.println(cis.equals(s));
        System.out.println(s.equals(cis));
    }
}

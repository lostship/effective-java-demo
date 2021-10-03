package com.example.effectivejava.i10equals.symmetry.bad;

import java.util.Objects;

public class CaseInsensitiveString {
    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CaseInsensitiveString) {
            return s.equalsIgnoreCase(((CaseInsensitiveString) obj).s);
        } else if (obj instanceof String) {
            return s.equalsIgnoreCase((String) obj);
        }
        return false;
    }

    /**
     * 违反了等价关系的对称性
     */
    public static void main(String[] args) {
        CaseInsensitiveString cis = new CaseInsensitiveString("ABC");
        String s = "abc";
        System.out.println(cis.equals(s));
        System.out.println(s.equals(cis));
    }
}

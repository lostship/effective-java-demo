package com.example.effectivejava.ch3commonmethod.i10equals.transitivity.bad;

import java.util.Objects;

public class SmellPoint extends Point {
    private final String smell;

    public SmellPoint(int x, int y, String smell) {
        super(x, y);
        this.smell = smell;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Point)) {
            return false;
        }

        if (!(obj instanceof SmellPoint)) {
            return obj.equals(this);
        }

        return super.equals(obj)
                && Objects.equals(((SmellPoint) obj).smell, smell);
    }
}

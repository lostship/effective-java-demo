package com.example.effectivejava.ch6enumandannotation.i37enummap;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Phase {
    SOLID,
    LIQUID,
    GAS,
    PLASMA; // 扩展

    public enum Transition {
        MELT(SOLID, LIQUID), // 熔化
        FREEZE(LIQUID, SOLID), // 凝固
        BOIL(LIQUID, GAS), // 汽化
        CONDENSE(GAS, LIQUID), // 液化
        SUBLIME(SOLID, GAS), // 升华
        DEPOSIT(GAS, SOLID), // 凝华
        IONIZATION(GAS, PLASMA), // 电离化
        DEIONIZATION(PLASMA, GAS); // 消电离化

        private final Phase from;
        private final Phase to;

        private static final Map<Phase, Map<Phase, Transition>> transitionMap = Stream.of(values()).collect(
                Collectors.groupingBy(
                        e -> e.from,
                        () -> new EnumMap<>(Phase.class),
                        Collectors.toMap(
                                e -> e.to,
                                e -> e,
                                (e1, e2) -> e2,
                                () -> new EnumMap<>(Phase.class))));

        Transition(Phase from, Phase to) {
            this.from = from;
            this.to = to;
        }

        public static Transition from(Phase from, Phase to) {
            return transitionMap.get(from).get(to);
        }
    }

    public static void main(String[] args) {
        System.out.println(Transition.from(LIQUID, GAS));
    }
}

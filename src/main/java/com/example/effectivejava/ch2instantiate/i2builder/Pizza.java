package com.example.effectivejava.ch2instantiate.i2builder;

import static com.example.effectivejava.ch2instantiate.i2builder.Pizza.Topping.*;
import static com.example.effectivejava.ch2instantiate.i2builder.NyPizza.Size.*;

import java.util.EnumSet;
import java.util.Set;

public abstract class Pizza {
    public enum Topping {
        HAM,
        MUSHROOM,
        ONION,
        PEPPER,
        SAUSAGE
    }

    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(topping);
            return self();
        }

        abstract Pizza build();

        // Subclasses must override this method to return "this"
        // 它和抽象的self方法一样，允许在子类中适当地进行方法链接，不需要转换类型。
        // 这个针对Java缺乏self类型的解决方案，被称作模拟的self类型(simulated self-type)。
        protected abstract T self();
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone();
    }

    protected Set<Topping> getToppings() {
        return toppings;
    }

    public static void main(String[] args) {
        NyPizza nyPizza = new NyPizza.Builder(SMALL).addTopping(SAUSAGE).addTopping(ONION).build();
        System.out.println(nyPizza);
        Calzone calzone = new Calzone.Builder().addTopping(HAM).sauceInside().build();
        System.out.println(calzone);
    }
}

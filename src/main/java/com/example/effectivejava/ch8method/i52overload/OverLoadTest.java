package com.example.effectivejava.ch8method.i52overload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 慎用重载
 * 
 * 重载方法的选择是静态的，在编译时根据参数编译时类型决定；
 * 重写方法的选择是动态的，在运行时根据参数运行时类型决定。
 * 
 * 设计不良的重载方法可能会造成理解混乱，以及难以意识到的错误使用。
 * 
 * 如果方法参数具有“根本不同”（radically different）的类型，重载不会造成问题。
 * 如果重载方法在同样的参数上被调用时，它们执行的是相同的功能，也不会带来危害
 * （参考String类的contentEquals，确保这种行为的标准做法是让更具体化的重载方法把调用转发给更一般化的重载方法。
 * 标准类库中也有违背这一思想的，比如String的valueOf(char[])和valueOf(Object)。
 * 实践中还是根据具体情况判断，使用易于理解和区分的良好设计）。
 * 
 * 否则就应该考虑重命名方法（参考ObjectIn/OutputStream中的readXXX方法）；
 * 如果是构造器无法重命名，可以考虑使用静态工厂。
 * 实践中尽量避免同一组参数只需进行类型转换就可以传递给不同重载方法的设计。
 * 
 * lambda和方法引用进一步增加了重载造成混淆的可能，不同的函数接口并非根本不同。
 * 不要在相同的参数位置重载带有不同函数接口的方法。
 * 
 */
public class OverLoadTest {
    public static void main(String[] args) {
        test1();
        test2();
    }

    private static void test1() {
        Collection<?>[] collections = {
                new HashSet<String>(),
                new ArrayList<String>(),
                new HashMap<String, String>().values()
        };
        for (Collection<?> c : collections) {
            // 重载方法的选择是在编译时确定的，
            // 元素编译时类型都是Collection，只会调用同一个方法，不会调用其它重载方法，。
            // 某些情况容易造成无意识的错误使用。
            System.out.println(CollectionClassifier.classify(c));
        }

        List<Wine> wineList = List.of(new Wine(), new SparklingWine(), new Champagne());
        for (Wine wine : wineList) {
            // 重写方法的选择是在运行时确定的。
            System.out.println(wine.name());
        }
    }

    private static void test2() {
        Set<Integer> set = new TreeSet<>();
        List<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; i++) {
            set.add(i);
            list.add(i);
        }
        System.out.println(set + "" + list);
        for (int i = 0; i < 3; i++) {
            set.remove(i);
            // list有两个重载的remove方法：remove(E)和remove(int)，
            // 由于自动装箱的存在，这两个方法不再“根本不同”，容易造成客户端理解和使用混乱。
            // （另外还有一点错误，使用下标循环中修改列表元素，下标和size变化没有匹配）
            list.remove(i);
        }
        System.out.println(set + "" + list);
    }
}

class CollectionClassifier {
    public static String classify(Set<?> s) {
        return "Set";
    }

    public static String classify(List<?> l) {
        return "List";
    }

    public static String classify(Collection<?> c) {
        return "Unknown Collection";
    }
}

class Wine {
    String name() {
        return "wine";
    }
}

class SparklingWine extends Wine {
    @Override
    String name() {
        return "sparkling wine";
    }
}

class Champagne extends SparklingWine {
    @Override
    String name() {
        return "champagne";
    }
}

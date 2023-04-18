package chap03;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public class Lambdas {

    public static void main(String... args) {
        // 간단한 예제
        Runnable r = () -> System.out.println("Hello!");
        r.run();

        // 익명 클래스
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello!");
            }
        };

        // 람다로 거름
        List<Apple> inventory = Arrays.asList(
                new Apple(80, Color.GREEN),
                new Apple(155, Color.GREEN),
                new Apple(120, Color.RED)
        );

        // [Apple{color=GREEN, weight=80}, Apple{color=GREEN, weight=155}]
        List<Apple> greenApples = filter(inventory, (Apple a) -> a.getColor() == Color.GREEN);
        System.out.println(greenApples);

        Comparator<Apple> c = (Apple a1, Apple a2) -> a1.getWeight() - a2.getWeight();

        // [Apple{color=GREEN, weight=80}, Apple{color=RED, weight=120}, Apple{color=GREEN, weight=155}]
        inventory.sort(c);
        System.out.println(inventory);

        Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();

        List<Integer> l = map(Arrays.asList("lambdas", "in", "action"), (String s) -> s.length());

        // true (박싱 없음)
        IntPredicate evenNumbers = (int i) -> i % 2 == 0;
        System.out.println(evenNumbers.test(1000));

        // false (박싱)
        Predicate<Integer> oddNumbers = (Integer i) -> i % 2 != 0;
        System.out.println(oddNumbers.test(1000));

        // add 메서드는 boolean을 반환
        List<String> list = new ArrayList<>();
        Predicate<String> p = s -> list.add(s);
        Consumer<String> b = s -> list.add(s);

        // 람다의 지역 변수 사용
        int portNumber = 1337;
        Runnable rb = () -> System.out.println(portNumber);
        rb.run();

        Runnable rb2 = () -> System.out.println(portNumber);
//        portNumber = 1111;                      // 컴파일 에러

    }

    public static List<Apple> filter(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    interface ApplePredicate {
        boolean test(Apple a);
    }

    // Predicate
    public <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> results = new ArrayList<>();
        for (T t: list) {
            if (p.test(t)) {
                results.add(t);
            }
        }
        return results;
    }

    // Consumer
    public <T> void forEach(List<T> list, Consumer<T> c) {
        for (T t: list) {
            c.accept(t);
        }
        forEach(Arrays.asList(1, 2, 3, 4, 5),(Integer i) -> System.out.println(i));
    }

    // Function
    public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();
        for (T t: list) {
            result.add(f.apply(t));
        }
        return result;
    }

    // 함수형 인터페이스와 예외
    @FunctionalInterface
    public interface BufferedReaderProcessor{
        String process(BufferedReader b) throws IOException;
    }

    BufferedReaderProcessor b = (BufferedReader br) -> br.readLine();

    Function<BufferedReader, String> f = (BufferedReader b) -> {
        try {
            return b.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    Callable<Integer> c = () -> 42;
    PrivilegedAction<Integer> p = () -> 42;

}

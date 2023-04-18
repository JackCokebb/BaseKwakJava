package chap02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;

public class FilteringApples {

    public static void main(String... args) {

        List<Apple> inventory = Arrays.asList(
                new Apple(80, Color.GREEN),
                new Apple(155, Color.GREEN),
                new Apple(120, Color.RED));

        // [Apple{color=GREEN, weight=80}, Apple{color=GREEN, weight=155}]
        List<Apple> greenApples = filterApplesByColor(inventory, Color.GREEN);
        System.out.println(greenApples);

        // [Apple{color=RED, weight=120}]
        List<Apple> redApples = filterApplesByColor(inventory, Color.RED);
        System.out.println(redApples);


        // 정의한 클래스의 객체를 만들어 전달

        // [Apple{color=GREEN, weight=80}, Apple{color=GREEN, weight=155}]
        List<Apple> greenApples2 = filter(inventory, new AppleColorPredicate());
        System.out.println(greenApples2);

        // [Apple{color=GREEN, weight=155}]
        List<Apple> heavyApples = filter(inventory, new AppleWeightPredicate());
        System.out.println(heavyApples);

        // []
        List<Apple> redAndHeavyApples = filter(inventory, new AppleRedAndHeavyPredicate());
        System.out.println(redAndHeavyApples);

        // filterApples 메서드의 동작을 직접 파라미터화 !!

        // [Apple{color=RED, weight=120}]
        List<Apple> redApples2 = filter(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple a) {
                return a.getColor() == Color.RED;
            }
        });
        System.out.println(redApples2);


        // 람다 표현식 사용
        List<Apple> result = filter(inventory, (Apple apple) -> Color.RED.equals(apple.getColor()));


        // 다양한 객체 사용 가능

        List<Integer> numbers = Arrays.asList(1, 2, 3);
        List<Apple> result2 = filter(inventory, (Apple apple) -> Color.RED.equals(apple.getColor()));
        List<Integer> evenNumbers = filterUsingGeneric(numbers, (Integer i) -> i % 2 == 0);



        inventory.sort(new java.util.Comparator<Apple>() {
            @Override
            public int compare(Apple a1, Apple a2) {
                return a1.getWeight().compareTo(a2.getWeight());
            }
        });

        // Comparator로 정렬하기
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

        // Runnable로 코드 블록 실행하기
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world");
            }
        });

        Thread t2 = new Thread(() -> System.out.println("Hello World"));

        // Callable을 결과로 반환하기
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> threadName = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Thread.currentThread().getName();
            }
        });

        Future<String> threadName2 = executorService.submit(() -> Thread.currentThread().getName());
    }


    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor() == Color.GREEN) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApplesByColor(List<Apple> inventory, Color color) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor() == color) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getWeight() > weight) {
                result.add(apple);
            }
        }
        return result;
    }

    // 메서드가 다양한 동작을 받을 수 있음!!
    public static List<Apple> filter(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }


    public static <T> List<T> filterUsingGeneric(List<T> inventory, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T e : inventory) {
            if (p.test(e)) {
                result.add(e);
            }
        }
        return result;
    }


    enum Color {
        RED,
        GREEN
    }

    public static class Apple {

        private int weight = 0;
        private Color color;

        public Apple(int weight, Color color) {
            this.weight = weight;
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        @SuppressWarnings("boxing")
        @Override
        public String toString() {
            return String.format("Apple{color=%s, weight=%d}", color, weight);
        }

    }

    // ApplePredicate라는 인터페이스를 작성하고 여러 조건을 가진 클래스를 작성
    interface ApplePredicate {

        boolean test(Apple a);

    }

    static class AppleWeightPredicate implements ApplePredicate {

        @Override
        public boolean test(Apple apple) {
            return apple.getWeight() > 150;
        }

    }

    static class AppleColorPredicate implements ApplePredicate {

        @Override
        public boolean test(Apple apple) {
            return apple.getColor() == Color.GREEN;
        }

    }

    static class AppleRedAndHeavyPredicate implements ApplePredicate {

        @Override
        public boolean test(Apple apple) {
            return apple.getColor() == Color.RED && apple.getWeight() > 150;
        }

    }

}

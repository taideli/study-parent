package com.tdl.study.java.test;

import org.apache.logging.log4j.util.Strings;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    public void t1() {
        Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE)).limit(10);
        integers.forEach(System.out::println);
    }

    @Test
    public void t2() {
        String string = "yields a stream whose elements are the parts of the input that are delimited by this pattern";
        Stream<String> stream = Pattern.compile(" ").splitAsStream(string);
        stream.forEach(System.out::println);
    }

    @Test
    public void t3() {
        System.out.println(Paths.get("").toAbsolutePath());
        try(Stream<String> stream = Files.lines(Paths.get("src/test/resources/file_4_collectors_test_t3.sql"))) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void t4() {
        Stream<String> words = Stream.of("good", " morning", " everyone");
        words.flatMap(word -> letters(word)).forEach(s -> System.out.println(s));
    }

    private static Stream<String> letters(String string) {
        List<String> list = new ArrayList<>();
        if (Strings.isEmpty(string)) return list.stream();
        for (int i = 0; i < string.length(); i++) {
            list.add(String.valueOf(string.charAt(i)));
        }
        return list.stream();
    }

    @Test
    public void t5() {
        Stream<String> stream = Stream.concat(letters("hello"), letters("world"));
        stream.forEach(System.out::println);
    }

    @Test
    public void t6() {
        Stream<String> uniqueWords = Stream.of("merrily", "merrily", "merrily", "gently").distinct();
        uniqueWords.forEach(System.out::println);
    }

    @Test
    public void t7() {
        Stream<String> uniqueWords = Stream.of("fds", "ew", "wr322", "a")
                .sorted(Comparator.comparing(String::length)/*.reversed()*/);
        uniqueWords.forEach(System.out::println);
    }

    @Test
    public void t8() {
        Object[] powers = Stream.iterate(1.0, p -> p * 2)
                .peek(e -> System.out.println("Fetching " + e))
                .limit(20).toArray();
    }

    @Test
    public void t9() {
        Stream<String> words = Pattern.compile(" ").splitAsStream("Now that you have seen how to create and transform streams, we will finally get to the most " +
                "important point—getting answers from the stream data. The methods that we cover in this " +
                "section are called reductions. Reductions are terminal operations. They reduce the stream to a " +
                "non-stream value that can be used in your program");
//        Optional<String> largest = words.max(String::compareToIgnoreCase);
        Optional<String> largest = words.max(Comparator.comparingInt(String::length));
        System.out.println("largest: " + largest.orElse(""));
    }

    @Test
    public void t10() {
        Stream<String> words = Pattern.compile(" ").splitAsStream("For collecting stream elements to another target, there is a convenient collect method that\n" +
                "takes an instance of the Collector interface. The Collectors class provides a large number of " + "factory methods for common collectors");
        Set<String> result = words.collect(Collectors.toSet());
        result.forEach(System.out::println);
    }

    @Test
    public void t11() {
        Stream<String> stream = Stream.of("If you want to reduce the stream results to a sum, average, maximum, or minimum, use one of the summarizing(Int|Long|Double) methods".split(" "));
        IntSummaryStatistics summary = stream.collect(Collectors.summarizingInt(String::length));
        double avg = summary.getAverage();
        double max = summary.getMax();
        System.out.println(avg);
        System.out.println(max);
        System.out.println(summary);
    }

    @Test
    public void t12() {
        Stream<String> stream = Stream.of("If you want to reduce the stream results to".split(" "));
        stream.iterator().forEachRemaining(s -> System.out.println(s));
    }

    @Test
    public void t13() {
        Stream.iterate(0, n -> n + 1).limit(10).iterator().forEachRemaining(n -> System.out.println(n));
    }

    public static class Person {
        private int id;
        private String name;
        private String gender;

        public Person(int id, String name, String gender) {
            this.id = id;
            this.name = name;
            this.gender = gender;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getGender() {
            return gender;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", " +
                    "name='" + name + "\'" +
                    ", " +
                    "gender='" + gender + "\'" +
                    '}';
        }
    }

    @Test
    public void t14() {
        Stream<Person> people = Stream.of(
                new Person(1, "aa", "M"),
                new Person(2, "bb", "M"),
                new Person(2, "22", "F"), // 有重复key时要指定toMap的第三个参数
                new Person(3, "cc", "F"),
                new Person(4, "dd","N"));
        // TODO: 2018/1/18 适用于key没有重复的数据
//        Map<Integer, Person> idToPerson = people.collect(Collectors.toMap(p -> p.id, p -> p));
//        Map<Integer, Person> idToPerson = people.collect(Collectors.toMap(p -> p.id, Function.identity()));
        // TODO: 2018/1/18 适用于key有重复的数据
//        Map<Integer, Person> idToPerson = people.collect(Collectors.toMap(p -> p.id, Function.identity(), (older, newer) -> older/*newer*/));
        // TODO: 2018/1/18 可以指定Map的构造函数, 默认是HashMap
        ConcurrentHashMap<Integer, Person> idToPerson = people.collect(
                Collectors.toMap(
                        p -> p.id,
                        Function.identity(),
                        (older, newer) -> older,
                        ConcurrentHashMap<Integer, Person>::new
                )
        );

        idToPerson.forEach((k, v) -> System.out.println(k + "-->" + v));
    }

    @Test
    public void t15() {
        Stream<Person> people = Stream.of(
                new Person(1, "aa", "M"),
                new Person(2, "bb", "M"),
                new Person(2, "22", "F"), // 有重复key时要指定toMap的第三个参数
                new Person(3, "cc", "F"),
                new Person(4, "dd","N"));
        Map<Integer, Set<String>> mss = people.collect(
                Collectors.toMap(
                        Person::getId,
                        l -> Collections.singleton(l.getGender()),
                        (a, b) -> {
                            Set<String> union = new HashSet<>(a);
                            union.addAll(b);
                            return union;
                        }
                )
        );
        System.out.println(mss);
    }

    @Test
    public void t16() {
        Stream<Person> people = Stream.of(
                new Person(1, "aa", "M"),
                new Person(2, "bb", "M"),
//                new Person(2, "22", "F"), // 有重复key时要指定toMap的第三个参数
                new Person(3, "cc", "F"),
                new Person(4, "dd","N"),
                new Person(5, "ba", "M"),
                new Person(6, "bc", "M"));

        // 性别 姓名 对象
        Map<String, Map<String, Person>> mmsp = people.collect(Collectors.toMap(
                Person::getGender,
                p -> {
                    Map<String, Person> m = new HashMap<>();
                    m.put(p.getName(), p);
                    return m;
                },
                (m1, m2) -> {
                    Map<String, Person> union = new HashMap<>(m1);
                    union.putAll(m2);
                    return union;
                }
        ));

        System.out.println(mmsp);
    }

    @Test
    public void t17() {
        Stream<Person> people = Stream.of(
                new Person(1, "aa", "M"),
                new Person(2, "bb", "M"),
//                new Person(2, "22", "F"), // 有重复key时要指定toMap的第三个参数
                new Person(3, "cc", "F"),
                new Person(4, "dd","N"),
                new Person(5, "ba", "M"),
                new Person(6, "bc", "M"));

        Map<String, List<Person>> ml = people.collect(Collectors.groupingBy(Person::getGender));
        System.out.println(ml);
    }

    @Test
    public void t18() {
        Stream<Person> people = Stream.of(
                new Person(1, "aa", "M"),
                new Person(2, "bb", "M"),
//                new Person(2, "22", "F"), // 有重复key时要指定toMap的第三个参数
                new Person(3, "cc", "F"),
                new Person(4, "dd","N"),
                new Person(5, "ba", "M"),
                new Person(6, "bc", "M"));

        Map<String, List<Person>> ml = people.collect(Collectors.groupingBy(
                p -> p.getGender()
        ));
        System.out.println(ml);
    }

    /**
     * flatMap test
     */
    @Test
    public void t19() {
        List<List<Integer>> lists = Stream.iterate(0, i -> i + 1).limit(100)
                .collect(Collectors.groupingBy(i -> i / 10)).values().stream().collect(Collectors.toList());
        System.out.println(lists);

        List<Integer> list = lists.stream().flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println(list);
    }

    @Test
    public void ty() {
        double similarity = 0.123456789098803d;
        DecimalFormat format = new DecimalFormat("####0.0000");
        String s = format.format(similarity);
        String r = s.substring(0, s.indexOf(".") + 4);
        System.out.println(r);
    }

    @Test
    public void tt() {
        Stream.of("2017", "2003", "2015").sorted((s1, s2) -> s2.compareTo(s1)).forEach(s -> System.out.println(s));
    }
}

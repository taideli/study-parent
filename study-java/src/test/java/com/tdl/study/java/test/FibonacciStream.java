package com.tdl.study.java.test;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class FibonacciStream {
    public static void main(String[] args) {
        Stream<Long> fibonacci = Stream.generate(new FibonacciSupplier());
        fibonacci.limit(30).forEach(l -> System.out.println(l));
    }

}

class FibonacciSupplier implements Supplier<Long> {

    long a = 0;
    long b = 1;

    @Override
    public Long get() {
        long x = a + b;
        a = b;
        b = x;
        return a;
    }
}

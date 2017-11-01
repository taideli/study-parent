package com.tdl.study.java.test;


import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * π/4 = 1 - 1/3 + 1/5 -1/7 + 1/9 ....
 */
public class PiStream {

    public static void main(String[] args) {
/*
        Stream<Double> pi = Stream.generate(new PiSupplier());
        pi
                .skip(1000000000)
                .limit(10)
                .forEach(d -> System.out.println(d));
*/


        Stream<Double> pi2 = Stream.generate(new PiSupplier());
        pi2
                .map(new EulerTransform())
                .map(new EulerTransform())
                .map(new EulerTransform())
                .skip(100)
                .limit(10)
                .forEach(d -> System.out.println(d));
    }
}

class PiSupplier implements Supplier<Double> {

    double sum = 0.0;
    double current = 1.0;
    boolean sign = true;

    @Override
    public Double get() {
        sum += (sign ? 4: -4) / this.current;
        this.current = this.current + 2.0;
        this.sign = ! sign;
        return sum;
    }
}


/**
 * 欧拉变换对级数加速
 *
 *
 */
class EulerTransform implements Function<Double, Double> {

    double n1 = 0.0;
    double n2 = 0.0;
    double n3 = 0.0;

    @Override
    public Double apply(Double t) {
        n1 = n2;
        n2 = n3;
        n3 = t;
        return n1 == 0.0 ? 0.0 : calc();
    }

    private double calc() {
        double d = n3 - n2;
        return n3 - d * d / (n1 - 2 * n2 + n3);
    }
}

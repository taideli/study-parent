package com.tdl.study.core.conf;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class RandomDoubleGenerator extends RandomValueGenerator {
    private double min, max;
    private String pattern;

    /**
     *
     * @param min min value
     * @param max max value
     * @param decimal decimal part len
     */
    public RandomDoubleGenerator(double min, double max, int decimal) {
        this.min = min(abs(min), abs(max));
        this.max = max(abs(min), abs(max));
        this.pattern = Stream.iterate(0, i -> i + 1).limit(decimal).map(i -> "0").collect(Collectors.joining("", "#.", ""));
    }

    @Override
    public String generate() {
        double multiple = new Random().nextDouble();
        double value = min + (max - min) * multiple;
        return new DecimalFormat(pattern).format(value);
    }
}

package com.tdl.study.core.conf;

import java.text.DecimalFormat;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class RandomDoubleGenerator extends RandomValueGenerator {
    private double min, max;

    public RandomDoubleGenerator(double min, double max) {
        this.min = min(abs(min), abs(max));
        this.max = max(abs(min), abs(max));
    }

    @Override
    public String generate() {
        double multiple = new Random().nextDouble();
        double value = min + (max - min) * multiple;
        return new DecimalFormat("#.0000000000000000").format(value);
    }
}

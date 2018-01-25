package com.tdl.study.core.conf;

import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class RandomIntegerGenerator extends RandomValueGenerator {
    private int min, max;

    public RandomIntegerGenerator(int min, int max) {
        this.min = min(abs(min), abs(max));
        this.max = max(abs(min), abs(max));
    }

    @Override
    public String generate() {
        int value = new Random().nextInt(max - min + 1);
        return String.valueOf(min + value);
    }
}

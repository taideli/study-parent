package com.tdl.study.core.conf;

import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class RandomLongGenerator extends RandomValueGenerator {
    private long min, max;

    public RandomLongGenerator(long min, long max) {
        this.min = min(abs(min), abs(max));
        this.max = max(abs(min), abs(max));
    }

    @Override
    public String generate() {
        long value = new Random().nextLong();
        return String.valueOf(min + abs(value) % (max - min));
    }
}

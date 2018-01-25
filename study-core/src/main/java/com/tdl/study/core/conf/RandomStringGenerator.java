package com.tdl.study.core.conf;

import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class RandomStringGenerator extends RandomValueGenerator {
    private static final String sources = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private int len;
    private final Random random;

    public RandomStringGenerator(int start, int end) {
        int min = min(abs(start), abs(end));
        int max = max(abs(start), abs(end));
        random = new Random();
        len = min + random.nextInt(max - min + 1);
    }

    private char next() {
        return sources.charAt(random.nextInt(sources.length() - 1));
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(next());
        }
        return sb.toString();
    }
}

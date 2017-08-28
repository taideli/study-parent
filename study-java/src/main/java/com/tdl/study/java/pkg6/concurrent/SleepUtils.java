package com.tdl.study.java.pkg6.concurrent;

import java.util.concurrent.TimeUnit;

public class SleepUtils {
    public static final void second(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static final void sleep(int value, TimeUnit unit) {
        try {
            long millis = TimeUnit.MILLISECONDS.convert(value, unit);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.tdl.study.core.utils;

import java.util.concurrent.TimeUnit;

public class Threads {

    public static boolean sleep(long millis) {
        return sleep(millis, TimeUnit.MILLISECONDS);
    }

    public static boolean sleep(long value, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(value));
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }
}

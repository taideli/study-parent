package com.tdl.study.java.pkg6.concurrent;

/**数据量太少，并发并不一定比串行快*/
public class ConcurrentTest {
    private static final long count = 10000000000L;

    public static void main(String args[]) throws InterruptedException {
        concurrency();
        serial();
    }

    private static void concurrency() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(() -> {
            int a = 0;
            for (long i = 0; i < count; i++) a += 5;
        });
        thread.start();

        int b = 0;
        for (long i = 0; i < count; i++) b--;

        long time = System.currentTimeMillis() - start;
        thread.join();
        System.out.println("concurrency: " + time);
    }

    public static void serial() {
        long start = System.currentTimeMillis();
        int a = 0;
        for (long i = 0; i < count; i++) a += 5;
        int b = 0;
        for (long i = 0; i < count; i++) b--;
        long time = System.currentTimeMillis() - start;
        System.out.println("serial: " + time);
    }
}

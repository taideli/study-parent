package com.tdl.study.java.pkg6.concurrent;

import java.util.concurrent.TimeUnit;

public class Shutdown {

    public static void main(String[] args) throws InterruptedException {
        Runner one = new Runner();
        Thread countThread = new Thread(one, "CountTread");
        countThread.start();

        TimeUnit.SECONDS.sleep(1);
        countThread.interrupt();

        Runner two = new Runner();
        countThread = new Thread(two, "CountThread");
        countThread.start();

        TimeUnit.SECONDS.sleep(1);
        two.cancel();
    }


    private static class Runner implements Runnable {
        private long i;
        // TODO volatile 读取到的是最新值。
        private volatile boolean on = true;

        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()) {
                i++;
            }
            // notes by taidl@2017/6/29_20:18 好好学习
            System.out.println("Count i = " + i);
        }

        public void cancel() {
            on = false;
        }

    }
}

/**
 * Created by Taideli on 2017/7/1.
 */
package com.tdl.study.java.pkg6.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * join thread.join() 会等待thread线程结束后再继续执行本线程。
 */
public class Join {
    public static void main(String[] args) throws InterruptedException {
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new Domino(previous), "Thread[" + i + "]");
            t.start();
            previous = t;
        }

        TimeUnit.SECONDS.sleep(3);
        System.out.println(Thread.currentThread().getName() +  " terminate.");
    }

    static class Domino implements Runnable {
        private Thread prev;

        public Domino(Thread prev) {
            this.prev = prev;
        }

        @Override
        public void run() {
            try {
                prev.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " terminate.");
        }
    }
}

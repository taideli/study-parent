package com.tdl.study.java.pkg6.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * 关键字 volatile 用法
 * Java支持多个线程同时访问一个对象或者对象的成员变量，由于每个线程可以拥有这个
 * 变量的拷贝，所以程序在执行过程中，一个线程看到的变量并不一定是最新的
 *
 * 关键字volatile可以用来修饰字段（成员变量），就是告知程序任何对该变量的访问均需要
 * 从共享内存中获取，而对它的改变必须同步刷新回共享内存，它能保证所有线程对变量访问
 * 的可见性
 */
public class Volatile {

    public static void main(String[] args) throws InterruptedException {
        Runner one = new Runner(1);
        Thread countThread = new Thread(one, "CountTread");
        countThread.start();

        TimeUnit.SECONDS.sleep(1);
        countThread.interrupt(); //优雅地结束一个线程

        Runner two = new Runner(2);
        countThread = new Thread(two, "CountThread");
        countThread.start();

        TimeUnit.SECONDS.sleep(2);
        two.cancel(); //优雅地结束一个线程
    }


    private static class Runner implements Runnable {
        private long i;
        private final int id;
        // notes by taidl@2017/6/30_10:06 查看类注释学习volatile用法
        private volatile boolean on = true;

        public Runner(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()) {
                i++;
            }
            // notes by taidl@2017/6/29_20:18 好好学习
            System.out.println("Count(" + id + ") i = " + i);
        }

        public void cancel() {
            on = false;
        }

    }
}

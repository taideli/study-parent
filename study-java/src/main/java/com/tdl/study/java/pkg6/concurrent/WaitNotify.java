package com.tdl.study.java.pkg6.concurrent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 在使用wait(), notify(), notifyAll()时要先对调用对象加锁
 * 在使用notify(), notifyAll()方法调用后，等待线程不会立即从wait返回，而是得notify(), notifyAll()的线程释放锁后才有机会返回
 */
public class WaitNotify {
    private static boolean flag = true;
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(() -> {
            synchronized (lock) {
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread() + " flag is true. wait @ " +
                                new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread() + " flag is false. running @ " +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }, "WaitThread");
        waitThread.start();

        TimeUnit.SECONDS.sleep(1);

        Thread notifyThread = new Thread(() -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock. notify @ " +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
                lock.notifyAll();
                flag = false;
                SleepUtils.second(5);
            }

            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock again. sleep @ " +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
                SleepUtils.second(5);
            }
        }, "NotifyThread");
        notifyThread.start();
    }
}

/**
 * 经典范式
 * 等待方
 * synchronized(对象) {
 *     while(条件不满足) {
 *         对象.wait();
 *     }
 *   处理逻辑。。。。
 * }
 *
 * 通知方
 * synchronized(对象) {
 *     改变条件
 *     对象.notify()
 * }
 * */
package com.tdl.study.java.pkg6.concurrent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 不建议使用suspend resume stop 这些方法，因为
 * suspend 会占着资源进入睡眠状态，容易引发死锁问题
 * resume
 * stop 不会保证线程的资源会被正常释放
 *
 * 应该使用 等待/通知机制
 */
public class Deprecated {
    public static void main(String[] args) throws InterruptedException {
        DateFormat f = new SimpleDateFormat("HH:mm:ss");
        Thread printThread = new Thread(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getName() + " run at " + f.format(new Date()));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "printThread");
        printThread.setDaemon(true);
        printThread.start();

        TimeUnit.SECONDS.sleep(3);
        printThread.suspend();
        System.out.println("main suspend " + printThread.getName() + " at " + f.format(new Date()));

        TimeUnit.SECONDS.sleep(3);
        printThread.resume();
        System.out.println("main resume " + printThread.getName() + " at " + f.format(new Date()));

        TimeUnit.SECONDS.sleep(3);
        printThread.stop();
        System.out.println("main stop " + printThread.getName() + " at " + f.format(new Date()));

        TimeUnit.SECONDS.sleep(3);
    }
}

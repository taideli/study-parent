package com.tdl.study.java.pkg6.concurrent;

/**
 * 演示死锁代码
 * C:\Users\taidl\Desktop>jps -l
 * 16208 sun.tools.jps.Jps
 * 17440 com.tdl.study.java.pkg6.concurrent.DeadLockDemo
 * 18256 org.jetbrains.jps.cmdline.Launcher
 * 10648
 *
 * C:\Users\taidl\Desktop>jstack 17440 > deadlock.stack
 * 查找关键字 BLOCKED 看下原因
 * */
public class DeadLockDemo {
    private static String A = "A";
    private static String B = "B";

    public static void main(String[] args) {
        new DeadLockDemo().deadLock();
    }

    private void deadLock() {
        Thread t1 = new Thread(() -> {
            synchronized (A) { //锁相互竞争
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {
                    System.out.println("into synchronized B");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (B) {
                synchronized (A) {
                    System.out.println("thread 2 sync....");
                }
            }
        });

        t1.start();
        t2.start();
    }
}

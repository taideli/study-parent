package com.tdl.study.java.pkg6.concurrent;

/**
 * 主线程在启动daemon线程后退出，此时JVM已没有非daemon线程，
 * JVM需要退出，导致jvm中所有Daemon线程立即退出，finally语句并没有执行
 *
 * 在构建Daemon线程时，不能依靠finally块中的内容来确保执行关闭或清理资源的逻辑
 */
public class Daemon {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                SleepUtils.second(10);
            } finally {
                // notes by taidl@2017/6/29_20:19 这条语句并没有执行
                System.err.println("DaemonThread finally run.");
            }
        });

        thread.setDaemon(true);
        thread.start();

        System.out.println("主线程退出");
    }
}

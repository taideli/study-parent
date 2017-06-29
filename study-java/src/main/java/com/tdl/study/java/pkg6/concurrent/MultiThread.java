package com.tdl.study.java.pkg6.concurrent;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 一个Java程序的运行不仅仅是main()方法的运行，而是main线程和多个其他线程的同时运行
 *
 * [6] Monitor Ctrl-Break
 * [5] Attach Listener
 * [4] Signal Dispatcher ---分发处理发送给JVM信号的线程
 * [3] Finalizer -----------调用对象finalize方法的线程
 * [2] Reference Handler ---清除引用的线程
 * [1] main ----------------main线程
 */
public class MultiThread {
    public static void main(String[] args) {
        /*获取Java线程管理MXBean*/
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        /*不需要获取同步的monitor和synchronizer信息，仅获取线程和线程堆栈信息*/
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo info : threadInfos) {
            System.out.println("[" + info.getThreadId() + "] " + info.getThreadName());
        }
    }
}

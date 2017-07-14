package com.tdl.study.java.pkg6.concurrent.thread.pool;

import java.util.concurrent.*;

/**
 *  Callable 可以返回一个结果，而Runnable不返回结果。
 */
public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = new ForkJoinPool();
        Future<String> future = es.submit(() -> "Callable result");


        System.out.println("future get: " + future.get());

    }
}

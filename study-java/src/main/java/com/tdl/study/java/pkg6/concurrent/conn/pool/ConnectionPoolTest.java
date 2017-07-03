/**
 * Created by Taideli on 2017/7/2.
 */
package com.tdl.study.java.pkg6.concurrent.conn.pool;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPoolTest {
    static ConnectionPool pool = new ConnectionPool(10);
    // 使用了CountDownLatch来确保ConnectionRunnerThread能够同时开始执行，并
    // 且在全部结束之后，才使main线程从等待状态中返回
    static CountDownLatch start = new CountDownLatch(1);
    static CountDownLatch end;

    public static void main(String[] args) throws InterruptedException {
//        int threadCount = 10;
//        int threadCount = 20;
//        int threadCount = 30;
//        int threadCount = 40;
        int threadCount = 50;
        end = new CountDownLatch(threadCount);
        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new ConnectionRunner(count, got, notGot), "ConnectionRunnerThread");
            thread.start();
        }
        start.countDown();
        end.await();
        System.out.println("total invoke: " + (threadCount * count));
        System.out.println("got connection: " + got);
        System.out.println("not got connection: " + notGot);
    }

    static class ConnectionRunner implements Runnable {
        int count;
        AtomicInteger got, notGot;

        public ConnectionRunner(int count, AtomicInteger got, AtomicInteger notGot) {
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException ignored) {}
            while (count > 0) try {
                // 从线程池中获取连接，如果1000ms内无法获取到，将会返回null
                // 分别统计连接获取的数量got和未获取到的数量notGot
                Connection connection = pool.fetchConnection(1000);
                if (null != connection) {
                    try {
                        connection.createStatement();
                        connection.commit();
                    } finally {
                        pool.releaseConnection(connection);
                        got.incrementAndGet();
                    }
                } else {
                    notGot.incrementAndGet();
                }
            } catch (Exception ignored) {} finally {
                count--;
            }
            end.countDown();
        }
    }
}

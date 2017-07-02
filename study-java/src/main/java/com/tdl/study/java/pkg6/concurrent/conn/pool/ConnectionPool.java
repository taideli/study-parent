/**
 * Created by Taideli on 2017/7/1.
 */
package com.tdl.study.java.pkg6.concurrent.conn.pool;

import java.sql.Connection;
import java.util.LinkedList;

public class ConnectionPool {
    private final LinkedList<Connection> pool = new LinkedList<>();

    public ConnectionPool(int initialSize) {
        if (initialSize > 0) for (int i = 0; i < initialSize; i++) {
            pool.addLast(ConnectionDriver.createConnection());
        }
    }

    public void releaseConnection(Connection connection) {
        if (null != connection) {
            synchronized (pool) {
                // 连接释放后需要进行通知，这样其他消费者能够感知到连接池中已经归还了一个连接
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }

    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            if (mills <= 0) {
                while (pool.isEmpty()) pool.wait();
                return pool.removeFirst();
            } else {
                long future = System.currentTimeMillis() + mills;
                long remaining = mills;
                while (pool.isEmpty() && remaining > 0) {
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                }
                return pool.isEmpty() ? null : pool.removeFirst();
            }

        }
    }
}

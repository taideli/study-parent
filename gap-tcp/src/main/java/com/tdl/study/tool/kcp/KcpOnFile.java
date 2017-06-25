/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool.kcp;

import java.util.function.Consumer;

public class KcpOnFile extends KCP {
    private boolean running;
    private final Object lock;
    private Thread writer;
    private Thread updater;
    private Consumer<byte[]> omit;

    public KcpOnFile(long conv_) {
        super(conv_);
        lock = new Object();
    }

    @Override
    protected void output(byte[] buffer, int size) {

    }


    public void start() {
        // TODO 先启动底层写文件线程 再启动接收线程
        if (null == omit) {
            throw new RuntimeException("omit has not been set.");
        }
        if (!running) {
            running = true;
            startReceiverThread();
            startUpdaterThread();
        } else {
            System.err.println("Kcp is already running");
        }
    }

    private void startReceiverThread() {
        writer = new Thread(() -> {
            while (running) {

            }
        });

        writer.start();
    }

    private void startUpdaterThread() {
        while (running) {
            long cur = System.currentTimeMillis();
            Update(cur);
        }
    }

    public void connect() {
        open();
    }
}

/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool.kcp;

import com.tdl.study.tool.IOs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class KcpOnFile extends KCP {
    private boolean running;
    private final Object lock;
    private Thread writer;
    private Thread updater;
    private Consumer<byte[]> omit;
    private Consumer<byte[]> using;
    private final Queue<byte[]> receives;

    public KcpOnFile(long conv_) {
        super(conv_);
        receives = new ConcurrentLinkedQueue<>();
        lock = new Object();
    }

    @Override
    protected void output(byte[] buffer, int size) {
        byte[] tmp;
        if (buffer.length != size) {
            tmp = Arrays.copyOf(buffer, size);
        } else {
            tmp = buffer;
        }
        omit.accept(tmp);
    }


    public void start() {
        // TODO 先启动接收线程, 再启动更新线程
        if (null == omit) {
            throw new RuntimeException("omit has not been set.");
        }
        if (null == using) {
            throw new RuntimeException("using has not been set.");
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
                if (!receives.isEmpty()) {
                    byte[] data = receives.remove();
                    using.accept(data);
                } else {
                    synchronized (this.lock) {
                        try {
                            lock.wait(interval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        writer.start();
    }

    private void startUpdaterThread() {
        while (running) {
            long cur = System.currentTimeMillis();
            Update(cur);
            int size = PeekSize();
            if (0 < size) {
                byte[] data = new byte[size];
                Recv(data);
                receives.add(data);
                lock.notify();
            }
        }
    }

    public void setOmit(Consumer<byte[]> omit) {
        this.omit = omit;
    }

    public void setUsing(Consumer<byte[]> using) {
        this.using = using;
    }

    public void connect() {
        open();
    }

    public static void save(byte[] data, OutputStream out) {
        try {
            IOs.writeBytes(out, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static long parseConv(byte[] data) {
        return KCP.ikcp_decode32u(data, 0);
    }
}

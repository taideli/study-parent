package com.tdl.study.core.io;

import com.tdl.study.core.parallel.Concurrents;

import java.util.concurrent.atomic.AtomicReference;

public class OpenableThread extends Thread implements Openable {

    private final AtomicReference<Status> status = new AtomicReference<>(Status.OTHER);
    private static final ThreadGroup g = new ThreadGroup("OpenableThreads");

    public OpenableThread(String name) {
        super(g, name);
        init();
    }

    public OpenableThread(Runnable target) {
        super(g, target);
        init();
    }

    public OpenableThread(Runnable target, String name) {
        super(g, target, name);
        init();
    }

    private void init() {
        setUncaughtExceptionHandler((t, e) -> logger().error(getName() + " failure.", e));
        opening(super::start);
    }

    @Override
    public final void run() {
        status.set(Status.RUNNING);
        try {
            exec();
        } finally {
            status.set(Status.STOPPED);
        }
    }

    protected void exec() {
        super.run();
    }

    @Override
    public final String name() {
        return getName();
    }

    @Override
    public void open() {
        Openable.super.open();
        while (status.get() == Status.OTHER)
            Concurrents.waitSleep(10);
    }

    @Override
//    public synchronized void start() {
    public void start() {
        open();
    }

    @Override
    public void close() {
        Openable.super.close();
        while (status.get() != Status.STOPPED)
            Concurrents.waitSleep();
    }

    public enum Status {
        OTHER, RUNNING, STOPPED
    }
}

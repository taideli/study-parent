package com.tdl.study.core.io;

import com.tdl.study.core.base.Named;
import com.tdl.study.core.lambda.Runnable;
import com.tdl.study.core.log.Loggable;
import com.tdl.study.core.parallel.Concurrents;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public interface Openable extends AutoCloseable, Loggable, Named {
    enum Status {
        CLOSED, OPENING, OPENED, CLOSING
    }

    default boolean opened() {
        return Opened.status(this).get() == Status.OPENED;
    }

    default boolean closed() {
        return Opened.status(this).get() == Status.CLOSED;
    }

    default void opening(Runnable handler) {
        Opened.OPENING.compute(this, (self, orig) -> {
            return null == orig ? handler : Runnable.merge(orig, handler);
        });
    }

    default void closing(Runnable handler) {
        Opened.CLOSING.compute(this, (self, orig) -> {
            return null == orig ? handler : Runnable.merge(orig, handler);
        });
    }

    default void open() {
        AtomicReference<Status> s = Opened.STATUS.computeIfAbsent(this, o -> new AtomicReference<>(Status.CLOSED));
        if (s.compareAndSet(Status.CLOSED, Status.OPENING)) {
            logger().trace("Opening...");
            Runnable h = Opened.OPENING.get(this);
            if (null != h) h.run();
            if (!s.compareAndSet(Status.OPENING, Status.OPENED))
                throw new RuntimeException("Opened failure cause status [" + s.get() + "] not OPENING");
        }
        if (s.get() != Status.OPENED)
            throw new RuntimeException("Start failure cause status [" + s.get() + "] not OPENED");
        logger().trace(name() + " Opened.");
    }

    @Override
    default void close() {
        AtomicReference<Status> s = Opened.status(this);
        if (s.compareAndSet(Status.OPENED, Status.CLOSING)) {
            logger().trace(name() + " closing...");
            Runnable h = Opened.CLOSING.get(this);
            if (null != h) h.run();
            s.compareAndSet(Status.CLOSING, Status.CLOSED);
        } // else logger().warn(name() + " closing again?");
        while (!closed())
            Concurrents.waitSleep(500, logger(), "Waiting for closing finished...");
        Opened.STATUS.remove(this);
        logger().trace(name() + " closed.");
    }

    class Opened {
        private final static Map<Openable, AtomicReference<Status>> STATUS = new ConcurrentHashMap<>();
        private final static Map<Openable, Runnable> OPENING = new ConcurrentHashMap<>(),
                                                     CLOSING = new ConcurrentHashMap<>();

        private Opened() {}

        private static AtomicReference<Status> status(Openable inst) {
            return STATUS.getOrDefault(inst, new AtomicReference<>(Status.CLOSED));
        }
    }
}

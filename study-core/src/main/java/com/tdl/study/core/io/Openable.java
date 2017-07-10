package com.tdl.study.core.io;

import com.tdl.study.core.base.Named;
import com.tdl.study.core.log.Loggable;

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

    @Override
    default void close() {

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

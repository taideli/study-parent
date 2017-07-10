package com.tdl.study.core.io;

import com.tdl.study.core.base.Namedly;
import com.tdl.study.core.parallel.Streams;
import com.tdl.study.core.parallel.Concurrents;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public abstract class OutputImpl<V> extends Namedly implements Output<V> {
    protected OutputImpl() {super();}

    protected OutputImpl(String name) {super(name);}

    protected abstract boolean enqueue(V item);

    @Override
    public long enqueue(Stream<V> items) {
        // if output is full, sleep a while and return with doing nothing.
        if (!Concurrents.waitSleep(this::full)) return 0;
        AtomicLong count = new AtomicLong(0);
        Streams.of(items).forEach(item -> {
            if (enqueue(item)) count.incrementAndGet();
        });
        return count.get();
    }
}

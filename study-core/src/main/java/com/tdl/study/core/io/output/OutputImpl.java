package com.tdl.study.core.io.output;

import com.tdl.study.core.base.Namedly;
import com.tdl.study.core.parallel.Concurrents;
import com.tdl.study.core.parallel.Streams;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public abstract class OutputImpl<V> extends Namedly implements Output<V> {

    private AtomicLong size = new AtomicLong(0);

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
        size.addAndGet(count.get());
        return count.get();
    }

    @Override
    public long size() {
        return size.get();
    }
}

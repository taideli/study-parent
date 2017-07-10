package com.tdl.study.core.io;

import com.tdl.study.core.base.Namedly;
import com.tdl.study.core.parallel.Streams;

import java.util.function.Function;
import java.util.stream.Stream;

public abstract class InputImpl<V> extends Namedly implements Input<V> {
    public InputImpl() {
        super();
    }

    public InputImpl(String name) {
        super(name);
    }

    protected abstract V dequeue();

    @Override
    public long dequeue(Function<Stream<V>, Long> using, long batchSize) {
        return using.apply(Streams.of(this::dequeue, batchSize, () -> empty() && opened()));
    }
}

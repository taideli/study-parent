package com.tdl.study.core.io;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Input<V> extends IO, Dequeue<V>, Supplier<V>, Iterator<V> {
    static Input<?> NULL = (using, batchSize) -> 0;

    @Override
    default long size() {return Long.MAX_VALUE;}

    @Override
    default long capacity() {return 0;}

    public static <T> Input<T> of(Iterator<? extends T> it) {
        return (using, batchSize) -> {
            Stream.Builder<T> b = Stream.builder();
            AtomicLong count = new AtomicLong();
            T t = null;
            while (it.hasNext()) {
                if (null != (t = it.next())) b.add(t);
                if (count.incrementAndGet() > batchSize) break;
            }
            using.apply(b.build());
            return count.get();
        };
    }

    public static <T> Input<T> of(Iterable<? extends T> collection) {
        return of(collection.iterator());
    }

    public static <T> Input<T> of(Supplier<? extends T> next, Supplier<Boolean> ending) {
//        return of()
        // TODO: 2017/7/7 fixme
        return null;
    }

    @Override
    default V get() {
        AtomicReference<V> ref = new AtomicReference<>();
        dequeue(s -> {
            ref.lazySet(s.findFirst().orElse(null));
            return 1L;
        }, 1);
        return ref.get();
    }

    @Override
    default boolean hasNext() {return !empty();}

    @Override
    default V next() {return get();}
}

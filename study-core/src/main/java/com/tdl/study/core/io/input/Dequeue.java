package com.tdl.study.core.io.input;

import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
public interface Dequeue<V> {
    long dequeue(Function<Stream<V>, Long> using, long batchSize);
}

package com.tdl.study.core.parallel;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Streams {

    public static final String SYSTEM_PROPERTY_KEY_PARALLE_ENABLE = "com.tdl.study.core.parallel.stream.enable";
    private static final boolean DEFAULT_PARALLEL_ENABLE =
            Boolean.parseBoolean(System.getProperty(SYSTEM_PROPERTY_KEY_PARALLE_ENABLE, "true"));
    public static final Predicate<Object> NOt_NULL = Objects::nonNull;


    public static <V> Stream<V> of(Stream<V> s) {
        if (DEFAULT_PARALLEL_ENABLE) s = s.parallel();
        return s.filter(NOt_NULL);
    }

    public static <V> Stream<V> of(Spliterator<V> it) {
        return StreamSupport.stream(it, DEFAULT_PARALLEL_ENABLE).filter(NOt_NULL);
    }

    public static <V> Stream<V> of(Supplier<V> get, long size, Supplier<Boolean> ending) {
        return Streams.of(new Suppliterator<>(get, size, ending));
    }
}

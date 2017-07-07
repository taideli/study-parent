package com.tdl.study.core.io.utils;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Streams {

    public static final String SYSTEM_PROPERTY_KEY_PARALLE_ENABLE = "com.tdl.study.core.parallel.stream.enable";
    private static final boolean DEFAULT_PARALLEL_ENABLE =
            Boolean.parseBoolean(System.getProperty(SYSTEM_PROPERTY_KEY_PARALLE_ENABLE, "true"));
    public static final Predicate<Object> NOt_NULL = Objects::nonNull;


    public static <V> Stream<V> of(Stream<V> s) {
        if (DEFAULT_PARALLEL_ENABLE) s = s.parallel();
        return s.filter(NOt_NULL);
    }
}

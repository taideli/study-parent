package com.tdl.study.crawler.output;

public interface Store<V> {
    void prepare();

    void write(final Iterable<V> items);

    void close();
}

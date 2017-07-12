package com.tdl.study.crawler.output;

import java.util.List;

public interface Store<V> {
    void prepare();

    long write(final List<V> items);

    void close();
}

package com.tdl.study.core.io.output;

import com.tdl.study.core.base.Namedly;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public abstract class BatchOutput<V> extends Namedly implements Output<V> {
    private int batchsize = 1000;

    public abstract long enqueue(List<V> items);

    public BatchOutput<V> batchsize(int batchsize) {
        this.batchsize = batchsize;
        return this;
    }
    @Override
    public long enqueue(Stream<V> items) {
        AtomicInteger size = new AtomicInteger(0);
        List<V> list = new ArrayList<>(batchsize);
        // TODO: 2017/7/24 unfinished....

        enqueue(list);
        return 0;
    }

}

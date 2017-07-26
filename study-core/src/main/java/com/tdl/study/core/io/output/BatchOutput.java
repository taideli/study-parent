package com.tdl.study.core.io.output;

import com.tdl.study.core.base.Namedly;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * process with batch, the largest batch is 3072 for jooq limited.
 * @param <V>
 */
public abstract class BatchOutput<V> extends Namedly implements Output<V> {
    private int batchsize = 1000;
    private AtomicLong count = new AtomicLong(0);

    public abstract long enqueue(List<V> items);

    public void batchsize(int batchsize) {
        this.batchsize = batchsize;
    }

    @Override
    public long enqueue(Stream<V> items) {
        AtomicLong count = new AtomicLong(0);

        Seq.seq(items).zipWithIndex().groupBy(tuple -> tuple.v2 / batchsize)
            .forEach((index, batch) ->
                count.addAndGet(
                    enqueue(batch.stream().map(Tuple2::v1).collect(Collectors.toList()))
                )
            );
        this.count.addAndGet(count.get());
        return count.get();
    }

    @Override
    public long size() {
        return count.get();
    }
}

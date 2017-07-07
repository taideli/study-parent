package com.tdl.study.hbase.io;

import com.tdl.study.core.base.Namedly;
import com.tdl.study.core.io.Input;
import org.apache.hadoop.hbase.client.Result;

import java.util.function.Function;
import java.util.stream.Stream;

public class HbaseInput extends Namedly implements Input<Result> {
    @Override
    public long dequeue(Function<Stream<Result>, Long> using, long batchSize) {
        return 0;
    }
}

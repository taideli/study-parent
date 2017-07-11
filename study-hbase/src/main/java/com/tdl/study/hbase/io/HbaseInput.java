package com.tdl.study.hbase.io;

import com.tdl.study.core.base.Namedly;
import com.tdl.study.core.io.input.Input;
import org.apache.hadoop.hbase.client.Result;

import java.util.function.Function;
import java.util.stream.Stream;

public class HbaseInput extends Namedly implements Input<Result> {
    @Override
    public long dequeue(Function<Stream<Result>, Long> using, long batchSize) {
        // TODO: 2017/7/10
        return 0;
    }
}

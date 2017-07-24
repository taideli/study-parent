package com.tdl.study.hbase.io.pump.output;

import com.tdl.study.core.io.output.BatchOutput;
import org.apache.hadoop.hbase.client.Get;

import java.util.List;

public class HBaseOutput extends BatchOutput<Get> {

    public HBaseOutput() {

        open();
    }

    @Override
    public long enqueue(List<Get> items) {
        return 0;
    }
}

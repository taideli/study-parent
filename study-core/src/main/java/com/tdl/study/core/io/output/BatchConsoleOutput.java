package com.tdl.study.core.io.output;

import java.util.List;

public class BatchConsoleOutput extends BatchOutput<String> {

    public BatchConsoleOutput() {
        super();
        open();
    }

    @Override
    public long enqueue(List<String> items) {
        long size = items.size();
        System.out.println("enqueue a batch with [" + size + "] items.");
        return size;
    }

    @Override
    public void close() {
        System.out.println(this.getClass().getSimpleName() + " enqueue [" + size() + "] items.");
        super.close();
    }
}

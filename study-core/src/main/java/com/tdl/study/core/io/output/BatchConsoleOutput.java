package com.tdl.study.core.io.output;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class BatchConsoleOutput extends BatchOutput<String> {
    private AtomicLong count = new AtomicLong(0);

    public BatchConsoleOutput() {
        super();
        open();
    }

    @Override
    public long enqueue(List<String> items) {
        long size = items.size();
        System.out.println("enqueue a batch with [" + size + "] items.");
        count.addAndGet(size);
        return size;
    }

    @Override
    public void close() {
        System.out.println(this.getClass().getSimpleName() + " enqueue [" + count.get() + "] items.");
        super.close();
    }
}

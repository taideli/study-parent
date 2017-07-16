package com.tdl.study.core.io.output;

import java.util.concurrent.atomic.AtomicLong;

/**
 * to console
 */
public class ConsoleOutput extends OutputImpl<String> {
    private AtomicLong count = new AtomicLong(0);

    public ConsoleOutput() {
        super();
        open();
    }

    @Override
    protected boolean enqueue(String item) {
        count.incrementAndGet();
        System.out.println(item);
        return true;
    }

    @Override
    public void close() {
        super.close();
        System.out.println("ConsoleOutput receive [" + count.get() + "] items.");
    }
}

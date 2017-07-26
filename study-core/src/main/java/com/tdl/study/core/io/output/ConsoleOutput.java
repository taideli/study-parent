package com.tdl.study.core.io.output;

import java.util.concurrent.atomic.AtomicLong;

/**
 * to console
 */
public class ConsoleOutput extends OutputImpl<String> {

    public ConsoleOutput() {
        super();
        open();
    }

    @Override
    protected boolean enqueue(String item) {
        System.out.println(item);
        return true;
    }

    @Override
    public void close() {
        super.close();
        System.out.println("ConsoleOutput enqueue [" + size() + "] items.");
    }
}

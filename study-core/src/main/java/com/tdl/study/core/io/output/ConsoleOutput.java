package com.tdl.study.core.io.output;

/**
 * to console
 */
public class ConsoleOutput extends OutputImpl<String> {
    private volatile long count = 0;

    public ConsoleOutput() {
        super();
        open();
    }

    @Override
    protected boolean enqueue(String item) {
        count++;
        System.out.println(item);
        return true;
    }

    @Override
    public void close() {
        super.close();
        System.out.println("ConsoleOutput receive [" + count + "] items.");
    }
}

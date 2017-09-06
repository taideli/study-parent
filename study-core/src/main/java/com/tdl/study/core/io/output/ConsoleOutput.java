package com.tdl.study.core.io.output;

import java.io.PrintStream;

/**
 * print string to Console
 */
public class ConsoleOutput extends OutputImpl<String> {
    private final PrintStream stream;

    /**
     * print string to Console
     * use System.out as default
     */
    public ConsoleOutput() {
        this(false);
    }

    /**
     * print string to Console
     * @param stderr true - use System.err; false - use System.out
     */
    public ConsoleOutput(boolean stderr) {
        super();
        stream = stderr ? System.err : System.out;
        open();
    }

    @Override
    protected boolean enqueue(String item) {
        stream.println(item);
        return true;
    }

    @Override
    public void close() {
        super.close();
        System.out.println("ConsoleOutput enqueue [" + size() + "] items.");
    }
}

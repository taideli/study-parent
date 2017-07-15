package com.tdl.study.core.test.pump;

import com.tdl.study.core.io.output.OutputImpl;

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
}

package com.tdl.study.hbase.io;

import com.tdl.study.core.io.OutputImpl;

/**
 * to console
 */
public class ConsoleOutput extends OutputImpl<String> {
    @Override
    protected boolean enqueue(String item) {
        System.out.println(item);
        return true;
    }
}

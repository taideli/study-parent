package com.tdl.study.core.test.pump;

import com.tdl.study.core.io.input.InputImpl;

import java.util.UUID;

public class StringInput extends InputImpl<String> {
    private long capacity;
    private volatile long ptr = 0;

    public StringInput() {
        this(Long.MAX_VALUE);
    }

    public StringInput(long capacity) {
        this.capacity = capacity;
    }

    @Override
    protected String dequeue() {
        ptr++;
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean empty() {
        return ptr >= capacity;
    }
}

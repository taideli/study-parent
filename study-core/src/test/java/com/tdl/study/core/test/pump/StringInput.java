package com.tdl.study.core.test.pump;

import com.tdl.study.core.io.input.InputImpl;

import java.util.UUID;

public class StringInput extends InputImpl<String> {
    private long capacity;
    private volatile long ptr = 0;

    public StringInput(long capacity) {
        this.capacity = capacity;
    }

    @Override
    protected String dequeue() {
        return ptr++ >= capacity ? null : UUID.randomUUID().toString();
    }
}

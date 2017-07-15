package com.tdl.study.core.test.pump;

import com.tdl.study.core.io.input.InputImpl;

import java.util.UUID;

public class RandomStringInput extends InputImpl<String> {
    private long capacity;
    private volatile long ptr = 0;

    public RandomStringInput() {
        this(Long.MAX_VALUE);
    }

    public RandomStringInput(long capacity) {
        super();
        open();
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

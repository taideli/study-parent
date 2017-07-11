package com.tdl.study.core.test.pump;

import com.tdl.study.core.io.input.InputImpl;

import java.util.UUID;

public class UuidInput extends InputImpl<UUID> {

    private long capacity;
    private volatile long size;

    public UuidInput(long capacity) {
        this.capacity = capacity;
    }

    @Override
    protected UUID dequeue() {
        System.out.println("uuid dequeue");
        return (size++ <= capacity) ? UUID.randomUUID() : null;
    }
}

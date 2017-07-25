package com.tdl.study.core.io.input;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RandomStringInput extends InputImpl<String> {
    private long capacity;
    private AtomicLong count = new AtomicLong(0);

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
        count.incrementAndGet();
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean empty() {
        return count.get() >= capacity;
    }

    @Override
    public void close() {
        System.out.println(this.getClass().getSimpleName() + " dequeue [" + count.get() + "] items.");
    }
}

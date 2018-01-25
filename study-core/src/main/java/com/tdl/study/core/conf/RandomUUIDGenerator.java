package com.tdl.study.core.conf;

import java.util.UUID;

public class RandomUUIDGenerator extends RandomValueGenerator {

    public RandomUUIDGenerator() {
        super();
    }

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}

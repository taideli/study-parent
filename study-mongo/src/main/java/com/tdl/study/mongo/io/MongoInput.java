package com.tdl.study.mongo.io;

import com.mongodb.DBObject;
import com.tdl.study.core.io.input.InputImpl;

public class MongoInput extends InputImpl<DBObject> {
    @Override
    protected DBObject dequeue() {
        return null;
    }
}

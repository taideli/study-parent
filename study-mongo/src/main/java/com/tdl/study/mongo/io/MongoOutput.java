package com.tdl.study.mongo.io;

import com.mongodb.DBObject;
import com.tdl.study.core.io.output.OutputImpl;

public class MongoOutput extends OutputImpl<DBObject> {
    @Override
    protected boolean enqueue(DBObject item) {
        return false;
    }
}

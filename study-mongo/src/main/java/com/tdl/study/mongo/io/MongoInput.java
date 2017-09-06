package com.tdl.study.mongo.io;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.tdl.study.core.io.input.InputImpl;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MongoInput extends InputImpl<Document> {
    private MongoClient client;
    private FindIterable<Document> it;
    private MongoCursor<Document> mc;
    private final ReentrantReadWriteLock lock;

    public MongoInput(String mongoURI, Bson... filters) {
        super();
        MongoClientURI uri = new MongoClientURI(mongoURI);
        client = new MongoClient(uri);
        String dbName = uri.getDatabase();
        String collection = uri.getCollection();
        lock = new ReentrantReadWriteLock();

        opening(() -> open(dbName, collection, filters));
        closing(this::closeMongo);
        open();
    }

    @Override
    protected Document dequeue() {
        if (lock.writeLock().tryLock()) {
            Document doc = mc.hasNext() ? mc.next() : null;
            lock.writeLock().unlock();
            return doc;
        }

        return null;
    }

    @Override
    public boolean empty() {
        if (lock.writeLock().tryLock()) {
            boolean empty = !mc.hasNext();
            lock.writeLock().unlock();
            return empty;
        }
        return false;
    }

    private void open(String database, String collection, Bson... filters) {
        Bson filter;
        MongoCollection<Document> coll = client.getDatabase(database).getCollection(collection);
        if (null == filters || 0 == filters.length) filter = null;
        else if (1 == filters.length) filter = filters[0];
        else filter = Filters.and(filters);
        it = null == filter ? coll.find() : coll.find(filter);
        mc = it.iterator();
        logger().info("find %d records", null == filter ? coll.count() : coll.count(filter));
    }

    public MongoInput batch(int size) {
        it.batchSize(size);
        return this;
    }

    public MongoInput limit(int size) {
        it.limit(size);
        return this;
    }

    private void closeMongo() {
        logger().info("closing mongo client.");
        client.close();
    }
}

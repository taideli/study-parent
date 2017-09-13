package com.tdl.study.mongo.io;

import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.connection.*;
import com.mongodb.selector.ServerSelector;
import com.tdl.study.core.io.output.BatchOutput;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class MongoOutputAsync extends BatchOutput<Document> {
    private MongoClient client;
    private MongoCollection<Document> collection;

    public MongoOutputAsync(String mongoURI) {
        super();
        ConnectionString cs = new ConnectionString(mongoURI);
        String dbName = cs.getDatabase();
        String colName = cs.getCollection();
        client = MongoClients.create(cs);
        client.getDatabase("db").createCollection("new", new SingleResultCallback<Void>() {
            @Override
            public void onResult(Void result, Throwable t) {
                System.out.println("create 'new', t = " + t);
            }
        });
        System.out.println("after create 'new'");
        collection = client.getDatabase(dbName).getCollection(colName);//if collection does not exist, create when first insert
        System.out.println(collection.getDocumentClass().getName());
        open();

    }

    @Override
    public long enqueue(List<Document> items) {

        collection.insertMany(items, (result, t) -> {
            System.out.println("enqueue... " + items.size());
            if (null != t) t.printStackTrace();
        });
        return items.size();
    }

    @Override
    public void close() {
//        client.close();
        super.close();
    }
}
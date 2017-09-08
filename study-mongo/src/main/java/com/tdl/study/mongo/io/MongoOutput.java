package com.tdl.study.mongo.io;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import com.tdl.study.core.io.output.BatchOutput;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MongoOutput extends BatchOutput<Document> {
    private MongoClient client;
    private MongoCollection<Document> collection;
    private UpdateOptions updateOptions;

    /**
     * press in data to mongo db
     * @param mongoURI the standard uri to connect mongo, see {@link com.mongodb.MongoClientURI}
     * @param upsert true if a new document should be inserted if there are no field '_id' in the document
     */
    public MongoOutput(String mongoURI, boolean upsert) {
        MongoClientURI uri = new MongoClientURI(mongoURI);
        String dbName = uri.getDatabase();
        String colName = uri.getCollection();

        client = new MongoClient(uri);
        collection = client.getDatabase(dbName).getCollection(colName);

        updateOptions = new UpdateOptions().upsert(upsert);
        open();

    }

    public MongoOutput(List<ServerAddress> addrs, String user, String pwd, String db, String col) {

    }

    @Override
    public long enqueue(List<Document> items) {
        if (null == items || 0 == items.size()) return 0;
        AtomicInteger count = new AtomicInteger(0);
        List<Document> documents = items.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (updateOptions.isUpsert()) {
            documents.forEach(d -> { try {
                String _id = d.getString("_id");
                if (null != _id) {
                    List<Bson> sets = new ArrayList<>();
                    d.forEach((k, v) -> {if ( !"_id".equals(k)) sets.add(Updates.set(k, v));});
                    UpdateResult ur = collection.updateOne(Filters.eq("_id", new ObjectId(_id)),
                        Updates.combine(sets), updateOptions);
                    if (1 == ur.getModifiedCount()) count.incrementAndGet();
                } else {
                    collection.insertOne(d);
                    count.incrementAndGet();
                }
            } catch (Exception e) {
                logger().error(e.getMessage());
            }});
        } else {
            collection.insertMany(documents);
            count.addAndGet(documents.size());
        }
        if (items.size() != documents.size()) logger().info("ignore {} null documents.", items.size() - documents.size());
        return count.get();
    }

    @Override
    public void close() {
        client.close();
        System.out.println(getClass().getSimpleName() + " enqueue " + size() + " records.");
        super.close();
    }
}

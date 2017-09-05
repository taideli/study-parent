package com.tdl.study.mongodb.pkg1;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDBJDBC {
    public static void main(String[] args) {
        try {
            ServerAddress serverAddress = new ServerAddress("172.16.16.232", 27017);
            List<ServerAddress> addrs = new ArrayList<>();
            addrs.add(serverAddress);

            MongoCredential credential =
                    MongoCredential.createScramSha1Credential("user4db", "!@#QAZ123qaz", "".toCharArray());
            List<MongoCredential> credentials = new ArrayList<>();
            credentials.add(credential);

            MongoClient mongoClient = new MongoClient(addrs, credentials);
            MongoDatabase mongoDatabase = mongoClient.getDatabase("db");
            System.out.println("connect to database successfully");

            // create collection
            mongoDatabase.createCollection("test");

            // get collection
            MongoCollection<Document> collection = mongoDatabase.getCollection("test");

            // insert document
            Document doc = new Document();
            doc.append("title", "MongoDB")
               .append("description", "database")
               .append("likes", 100)
               .append("by", "Fly");

            List<Document> documents = new ArrayList<>();
            documents.add(doc);
            collection.insertMany(documents);
//            collection.insertOne(doc);

            //find documents
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> cursor = findIterable.iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }

            // update
            collection.updateMany(Filters.eq("likes", 100), new Document("likes", "200"));


            // delete
            collection.deleteOne(Filters.eq("likes", 200));
            collection.deleteMany(Filters.eq("likes", 200));

            mongoClient.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}

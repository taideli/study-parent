package com.tdl.study.mongodb.pkg1;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

public class MongoDriverTest {

    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder()
                .applicationName(MongoDriverTest.class.getSimpleName())
                .connectTimeout(10)
                .heartbeatSocketTimeout(3)
                .build();
//    MongoClient client = new MongoClient("172.16.16.232", options);

//    MongoClient client = new MongoClient(new MongoClientURI("mongodb://172.16.16.232:27017"));
        MongoCredential credential = MongoCredential.createCredential("user4db", "db", "!@#QAZ123qaz".toCharArray());
//        MongoClient client = new MongoClient("172.16.16.232", 27017);
        MongoClient client = new MongoClient(new ServerAddress("172.16.16.232", 27017), Arrays.asList(credential));
        MongoDatabase database = client.getDatabase("db");
        MongoCollection<Document> collection = database.getCollection("col");

        Document doc = new Document()
                .append("title", "kafka")
                .append("description", "kafka 是一个高性能的消息队列中间件")
                .append("by", "apache")
                .append("url", "http://www.kafka.apache.org")
                .append("tags", Arrays.asList("apache", "kafka", "message"))
                .append("likes", String.valueOf(99.9));

        collection.insertOne(doc);

        System.out.println(collection.count());

//        collection.find().first().forEach((d, o) -> System.out.println(d + " " + o));
        System.out.println(collection.find().first().toJson());
    }
}

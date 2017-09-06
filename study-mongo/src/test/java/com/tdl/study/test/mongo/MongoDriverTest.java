package com.tdl.study.test.mongo;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

public class MongoDriverTest {

    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder()
                .applicationName(MongoDriverTest.class.getSimpleName())
                .build();

        System.out.println("options:\n" + options.toString());
//    MongoClient client = new MongoClient("172.16.16.232", options);

//    MongoClient client = new MongoClient(new MongoClientURI("mongodb://172.16.16.232:27017"));
        MongoCredential credential = MongoCredential.createCredential("user4db", "db", "!@#QAZ123qaz".toCharArray());
//        MongoClient client = new MongoClient("172.16.16.232", 27017);
        MongoClient client = new MongoClient(new ServerAddress("172.16.16.232", 27017),
                Arrays.asList(credential), options);
        MongoDatabase database = client.getDatabase("db");
        MongoCollection<Document> collection = database.getCollection("col");

        Document doc = new Document()
                .append("title", "kafka")
                .append("description", "kafka 是一个高性能的消息队列中间件")
                .append("by", "apache")
                .append("url", "http://www.kafka.apache.org")
                .append("tags", Arrays.asList("apache", "kafka", "message"))
                .append("likes", String.valueOf(99.9));

//        collection.insertOne(doc);

//        collection.updateOne(Filters.eq("title", "kafka"), doc);
        FindIterable<Document> it = collection.find();
        it.forEach((Block<Document>) document -> System.out.println(document.toJson()));
        /*iterable.forEach((Consumer<Document>) document -> {
            document.
        });*/
        client.close();

        System.out.println("======");
        Bson filtersAnd = Filters.and(Filters.eq("f1", "v1"), Filters.lt("f2", "99"));
        Bson filtersOr = Filters.or(Filters.eq("f1", "v1"), Filters.lt("f2", "99"));
        System.out.println(filtersAnd);
        System.out.println(filtersOr);
        System.out.println(Filters.and());
    }
}

package com.tdl.study.mongo.io;

import com.mongodb.client.model.Filters;
import com.tdl.study.core.io.output.ConsoleOutput;
import com.tdl.study.core.io.pump.Pump;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ConsoleFromMongo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String user = URLEncoder.encode("user4db", "utf-8");
        String pwd = URLEncoder.encode("!@#QAZ123qaz", "utf-8");
        String db = "db";
//        String collection = "col";
        String collection = "yellow_tripdata";
        String mongoUri = String.format("mongodb://%s:%s@172.16.16.232:27017/%s.%s", user, pwd, db, collection);

        Bson titleFilter = Filters.eq("title", "kafka");
        Bson byFilter = Filters.eq("by", "apache");

        MongoInput input = new MongoInput(mongoUri, 78, 789, /*titleFilter, byFilter*/null);
        ConsoleOutput output = new ConsoleOutput();
        Pump pump = Pump.pump(input.then(Document::toJson), 2, output);
        pump.open();
    }
}













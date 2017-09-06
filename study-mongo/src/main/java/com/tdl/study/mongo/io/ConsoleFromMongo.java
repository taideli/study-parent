package com.tdl.study.mongo.io;

import com.tdl.study.core.io.output.ConsoleOutput;
import com.tdl.study.core.io.pump.Pump;
import org.bson.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ConsoleFromMongo {
    public static void main(String[] args) throws UnsupportedEncodingException {
//        MongoInput input = new MongoInput("mongodb://user4db:!%40%23QAZ123qaz@172.16.16.232:27017/db.col")
        String user = "user4db";
        String pwd = URLEncoder.encode("!@#QAZ123qaz", "utf-8");
        String mongoUri = String.format("mongodb://%s:%s@172.16.16.232:27017/db.yellow_tripdata", user, pwd);

        MongoInput input = new MongoInput(mongoUri).limit(1000).batch(100);
        ConsoleOutput output = new ConsoleOutput();
        Pump pump = Pump.pump(input.then(Document::toJson), 2, output);
        pump.open();
    }
}













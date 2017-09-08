package com.tdl.study.mongo.io;

import com.tdl.study.core.io.input.RandomStringInput;
import com.tdl.study.core.io.pump.Pump;
import org.bson.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

public class MongoFromRandomString {

    public static void main(String[] args) throws UnsupportedEncodingException {
        RandomStringInput input = new RandomStringInput(12345);
        String mongoUri = String.format("mongodb://%s:%s@172.16.16.232:27017/db.test",
                URLEncoder.encode("user4db", "UTF-8"), URLEncoder.encode("!@#QAZ123qaz", "UTF-8"));
//        MongoOutputAsync output = new MongoOutputAsync(mongoUri);
        MongoOutput output = new MongoOutput(mongoUri, true);
        output.batchsize(345);

        Pump pump = Pump.pump(input.then(s -> new Document()
//                .append("_id", "59b235a0349644288c11f197")
                .append("name", s)
//                .append("aa", UUID.randomUUID().toString())
//                .append("bb", UUID.randomUUID().toString())
//                .append("cc", UUID.randomUUID().toString())
                ), 3, output);
        pump.open();
    }
}

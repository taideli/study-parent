package com.tdl.study.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.stream.Stream;

public class Main {

    public static void main(String args[]) throws IOException {

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.newInstance(conf);

        String path = "/b5810e656c372075a58e5f1dfb1310b5/f/d7bd2a511d6a422d8a20cd3e4643d261\n";


//        Stream.of(fs.listStatus(new Path(path))).map(FileStatus::getPath);
//        Stream.of(fs.listStatus(new Path(path))).flatMap(FileStatus::getPath);

//        HFile.Reader reader = HFile.createReader()
    }
}

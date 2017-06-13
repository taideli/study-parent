package com.tdl.study.hdfs.pkg1.hello;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.stream.Stream;

public class Test {

    public static void main(String args[]) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.newInstance(conf);

        Stream.of(fs.listStatus(new Path("/")))
                .map(FileStatus::getPath).forEach(System.out::println);

        fs.close();
    }
}

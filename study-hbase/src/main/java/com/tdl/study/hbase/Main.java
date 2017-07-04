package com.tdl.study.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.io.hfile.CacheConfig;
import org.apache.hadoop.hbase.io.hfile.HFile;
import org.apache.hadoop.hbase.io.hfile.HFileScanner;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class Main {

    public static void main(String args[]) throws IOException {

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.newInstance(conf);

        String path = "/b5810e656c372075a58e5f1dfb1310b5/f/d7bd2a511d6a422d8a20cd3e4643d261";

        HFile.Reader reader = HFile.createReader(fs, new Path(path), new CacheConfig(conf), conf);
        HFileScanner scanner = reader.getScanner(false, false);
        reader.length();
        Cell cell = scanner.getKeyValue();

        System.out.println("row: " + Bytes.toString(CellUtil.cloneRow(cell)));
        System.out.println("f&q: " + Bytes.toString(CellUtil.cloneFamily(cell)) + ":" + Bytes.toString(CellUtil.cloneQualifier(cell)));
        System.out.println("val: " + Bytes.toString(CellUtil.cloneValue(cell)));
//        Stream.of(fs.listStatus(new Path(path))).map(FileStatus::getPath);
//        Stream.of(fs.listStatus(new Path(path))).flatMap(FileStatus::getPath);

//        HFile.Reader reader = HFile.createReader()
    }
}

package com.tdl.study.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.KeyValue.Type;
import org.apache.hadoop.hbase.io.hfile.CacheConfig;
import org.apache.hadoop.hbase.io.hfile.HFile;
import org.apache.hadoop.hbase.io.hfile.HFileScanner;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class Main {

    public static void main(String args[]) throws IOException {

        if (args.length < 2) {
            System.out.println("Usage: Main <isLocalFs> <path>");
            System.exit(1);
        }
        Configuration conf = new Configuration();
        FileSystem fs;
        if (Boolean.valueOf(args[0])) {
            fs = FileSystem.newInstanceLocal(conf);
        } else {
            fs = FileSystem.newInstance(conf);
        }
        String path = args[1];

        HFile.Reader reader = HFile.createReader(fs, new Path(path), new CacheConfig(conf), conf);
        System.out.println("length:" + reader.length());
        System.out.println("entries: " + reader.getEntries());

        reader.loadFileInfo();
        String algorithm = reader.getCompressionAlgorithm().getName();
        System.out.println("compression algorithm name: " + algorithm);

        HFileScanner scanner = reader.getScanner(false, false);
        // seekTo 必须要先调用
        if (!scanner.seekTo()) {
            System.out.println("empty hfile");
        }
        do {
            Cell cell = scanner.getKeyValue();

            Type type = Type.codeToType(cell.getTypeByte());
            if (cell.getTypeByte() >= Type.Delete.getCode() && cell.getTypeByte() <= Type.DeleteFamily.getCode()) {
                System.out.println("rowkey: " + new String(CellUtil.cloneRow(cell)) + " has been deleted");
                continue;
            }
            System.out.println("row: " + Bytes.toString(CellUtil.cloneRow(cell)));
            System.out.println("f:q: " + Bytes.toString(CellUtil.cloneFamily(cell)) + ":" + Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println("val: " + Bytes.toString(CellUtil.cloneValue(cell)));

        } while (scanner.next());
    }
}

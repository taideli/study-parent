package com.tdl.study.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;

public class FileCopyWithProcess {
    public static void main(String args[]) throws IOException {
        String localSrc = args[0];
        String dst = args[1];

        InputStream in = new BufferedInputStream(new FileInputStream(localSrc));

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        /*每写64KB打印一个. 一般用不到，但在MR任务中用到*/
        OutputStream out = fs.create(new Path(dst), () -> System.out.print("."));

        IOUtils.copyBytes(in, out, 4096, true);
    }
}

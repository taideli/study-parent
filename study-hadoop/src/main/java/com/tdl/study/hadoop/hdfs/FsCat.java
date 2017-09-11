package com.tdl.study.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class FsCat {

    /**
     * 运行方式：
     *  hadoop jar ./libs/study-hdfs-1.0.jar com.tdl.study.hdfs.pkg3.fs.FsCat /anaconda-ks.cfg
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        InputStream in = fs.open(new Path(args[0]));
        IOUtils.copyBytes(in, System.out, 4096, false);
        IOUtils.closeStream(in);

        /*支持随机读写*/
        FSDataInputStream fsin = fs.open(new Path(args[0]));
        fsin.seek(100);//开销比较高

        int size = 300;
        byte[] buf = new byte[size];
        int ret = fsin.read(buf, 0, size);
        IOUtils.copyBytes(fsin, System.out, 4096, true);
    }
}

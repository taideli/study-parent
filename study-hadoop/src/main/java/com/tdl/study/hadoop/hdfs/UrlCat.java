package com.tdl.study.hadoop.hdfs;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Deprecated
public class UrlCat {

    static {
        /*每个虚拟机只能调用一次这个方法，因此常在静态代码块中调用*/
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }

    /**
     * 测试方法,这个方法不推荐
     * hadoop jar study-hadoop-1.0.jar com.tdl.study.hadoop.hdfs.UrlCat hdfs:/user/hadoop_fs_help.txt
     * @param args url
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        InputStream in = null;
        try {
            in = new URL(args[0]).openStream();
            IOUtils.copyBytes(in, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(in);
        }
    }
}

package com.tdl.study.hbase;

import com.tdl.study.core.log.Loggable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class HBases implements Loggable {
    final static ExecutorService ex = Executors.newCachedThreadPool();

    public static Connection connect() throws IOException {
        return connect(null);
    }

    public static Connection connect(Map<String, String> conf) throws IOException {
        Configuration hconf = HBaseConfiguration.create();
        if (null != conf && !conf.isEmpty()) for (Map.Entry<String, String> entry : conf.entrySet()) {
            hconf.set(entry.getKey(), entry.getValue());
        }
        return ConnectionFactory.createConnection(hconf);
    }

    public static byte[][] splits(String... keys) {
        byte[][] result = new byte[keys.length][];
        for (int i = 0; i < keys.length; i++) {
            result[i] = Bytes.toBytes(keys[i]);
        }

        return result;
    }

    // ROWKEY 全为数字时，?~0的region中没有数据。
    public static byte[][] SPLITS_OTC = splits("1 2 3 4 5 6 7 8 9".split(" "));
    public static byte[][] SPLITS_HEX = splits("1 2 3 4 5 6 7 8 9 0 a b c d e f".split(" "));
}

package com.tdl.study.hbase;

import com.tdl.study.core.log.Loggable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

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
}

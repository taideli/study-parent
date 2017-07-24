package com.tdl.study.hbase.io.pump.input;

import com.tdl.study.core.base.Namedly;
import com.tdl.study.core.io.input.Input;
import com.tdl.study.core.io.input.InputImpl;
import com.tdl.study.hbase.HBases;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Stream;

public class HbaseInput extends Namedly implements Input<Result> {
    private Connection connection;
    private String tablename;
    private Table table;
    private ResultScanner scanner;
    private ReentrantReadWriteLock scannerLock;
    private AtomicBoolean end;

    public HbaseInput(final String tablename, byte[] startRow, byte[] stopRow) throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        connection = HBases.connect();
        this.tablename = tablename;
        table = connection.getTable(TableName.valueOf(tablename), es);
        Scan scan = new Scan(startRow, stopRow);
        scanner = table.getScanner(scan);
        scannerLock = new ReentrantReadWriteLock();
        end = new AtomicBoolean(false);
    }


    @Override
    public long dequeue(Function<Stream<Result>, Long> using, long batchSize) {
        if (!end.get() && scannerLock.writeLock().tryLock()) {
            Result[] rs = null;
            try {
                rs = scanner.next((int) batchSize);
            } catch (IOException e) {
                logger().warn("HBase failure", e);
            } finally {
                scannerLock.writeLock().unlock();
            }

            if (null != rs) {
                end.set(rs.length == 0);
                if (rs.length > 0) return using.apply(Stream.of(rs));
            }
        }
        return 0;
    }

    @Override
    public boolean empty() {
        return end == null || end.get();
    }

    @Override
    public void close() {
        if (null != scanner) scanner.close();
        try {
            if (null != table) table.close();
        } catch (Exception ignore) {}
        try {
            if (null != connection) connection.close();
        } catch (Exception ignore) {}
    }
}

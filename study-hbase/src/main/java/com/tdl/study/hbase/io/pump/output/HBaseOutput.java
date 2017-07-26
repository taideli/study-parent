package com.tdl.study.hbase.io.pump.output;

import com.tdl.study.core.io.output.BatchOutput;
import com.tdl.study.hbase.HBases;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class HBaseOutput extends BatchOutput<Put> {
    private final Connection conn;
    private final TableName tablename;

    public HBaseOutput(String table) throws IOException {
        super();
        this.tablename = TableName.valueOf(table);
        conn = HBases.connect();
        createTableIfAbsent(tablename, "f");
        open();
    }

    private void createTableIfAbsent(TableName table, String... families) throws IOException {
        Admin admin = conn.getAdmin();
        if (! admin.tableExists(table)) {
            HTableDescriptor desc = new HTableDescriptor(table);
            Stream.of(families).map(HColumnDescriptor::new).forEach(desc::addFamily);
            admin.createTable(desc, HBases.SPLITS_OTC);
        }
        admin.close();
    }

    @Override
    public long enqueue(List<Put> items) {
        if (null == items || items.size() == 0) return 0;
        try (Table table = conn.getTable(tablename)) {
            table.put(items);
        } catch (IOException e) {
            //todo failover: move to another thread and retry
            logger().info("put error, failover ....");
            return 0;
        }
        return items.size();
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (IOException ignored) {}
        super.close();
        logger().info(getClass().getSimpleName() + " enqueue [" + size() + "] items.");
    }
}

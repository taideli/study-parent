/**
 * Created by Taideli on 2017/7/9.
 */
package com.tdl.study.tools.io.pump;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.security.User;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CSV2HBase {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        HBaseAdmin admin = new HBaseAdmin(conf);

        TableName table = TableName.valueOf("NewYorkYellowTripData");
        if (!admin.tableExists(table)) {
            HTableDescriptor descriptor = new HTableDescriptor(table);
            HColumnDescriptor family = new HColumnDescriptor("f");
            descriptor.addFamily(family);
            admin.createTable(descriptor);
            System.out.println("table " + table.getNameAsString() + " created....");
        }

        Configuration hconf = new Configuration();
        FileSystem fs = FileSystem.get(hconf);
        InputStream is = fs.open(new Path("/yellow_tripdata_2016-12.csv"));
        Reader reader = new InputStreamReader(is);//
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        parser.getHeaderMap().forEach((str, num) -> {
            System.out.println(num + "  ->  " + str);
        });

        ExecutorService es = Executors.newCachedThreadPool();
        User user = User.getCurrent();
        Connection connection = ConnectionFactory.createConnection();
        Table t = connection.getTable(table);
        parser.getRecords().parallelStream()
                .map(CSV2HBase::csv2put)
                .forEach(put -> {
                    try {
                        t.put(put);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        reader.close();
        connection.close();
        /*CSVRecord record = parser.getRecordNumber()
        Put put = new Put("");*/

        admin.close();
    }

    public static Put csv2put(CSVRecord record) {
        String recordNum = String.format("%32s", record.getRecordNumber());
        String rowkey = recordNum.charAt(recordNum.length()-1) + recordNum;
        Put put = new Put(rowkey.getBytes());
//        record.toMap().forEach((k, v) -> {
//            put.add(CellUtil.createCell())
//        });
        return put;
    }
}

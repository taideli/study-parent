package com.tdl.study.tools.io.pump;

import com.tdl.study.core.io.pump.Pump;
import com.tdl.study.hbase.io.pump.output.HBaseOutput;
import com.tdl.study.tools.io.pump.input.CsvInput;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.UUID;

public class HBaseFromCsv {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: <csv> <table>");
            System.exit(1);
        }

        CsvInput input = new CsvInput(args[0]);
        HBaseOutput output = new HBaseOutput(args[1]);
        output.batchsize(2048);

        Pump pump = Pump.pump(input.then(record -> {
            String key = record.getRecordNumber() + UUID.randomUUID().toString();
            Put put = new Put(Bytes.toBytes(key), System.currentTimeMillis());
            record.toMap().forEach((k, v) -> put.addColumn(Bytes.toBytes("f"), Bytes.toBytes(k), Bytes.toBytes(v)));
            return put;
        }), 4, output);

        pump.open();
    }
}

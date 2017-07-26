package com.tdl.study.tools.io.pump;

import com.tdl.study.core.io.pump.Pump;
import com.tdl.study.hbase.io.pump.output.HBaseOutput;
import com.tdl.study.tools.io.pump.input.CsvInput;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class HBaseFromCsv {
    private static byte[] HBASE_TABLE_FAMILY = Bytes.toBytes("f");
    private static byte[] HBASE_TABLE_QUALIFIER = Bytes.toBytes("v");

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: <csv> <table>");
            System.exit(1);
        }

        CsvInput input = new CsvInput(args[0]);
        HBaseOutput output = new HBaseOutput(args[1]);
        output.batchsize(2048);

        Pump pump = Pump.pump(input.then(record -> {
            String key = (record.getRecordNumber() % 10) + String.format("%015d", record.getRecordNumber());
            Put put = new Put(Bytes.toBytes(key));
            List<String> fields = record.toList();
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < fields.size(); i++) {
                value.append(fields.get(i));
                if (i != fields.size()-1) value.append('\t');
            }
            put.addColumn(HBASE_TABLE_FAMILY, HBASE_TABLE_QUALIFIER, Bytes.toBytes(value.toString()));
            return put;
        }), 4, output);

        pump.open();
    }
}

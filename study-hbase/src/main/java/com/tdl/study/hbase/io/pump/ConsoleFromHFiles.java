package com.tdl.study.hbase.io.pump;

import com.tdl.study.core.io.output.ConsoleOutput;
import com.tdl.study.core.io.pump.Pump;
import com.tdl.study.hbase.io.pump.input.HFilesInput;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class ConsoleFromHFiles {
    public static void main(String[] args) throws IOException {
        HFilesInput input = new HFilesInput(false, args[0]);
        ConsoleOutput output = new ConsoleOutput();

        Pump pump = Pump.pump(input.then(cell ->
            String.format("%s %s:%s %s",
                Bytes.toString(CellUtil.cloneRow(cell)),
                Bytes.toString(CellUtil.cloneFamily(cell)),
                Bytes.toString(CellUtil.cloneQualifier(cell)),
                Bytes.toString(CellUtil.cloneValue(cell)))),
            3, output);
        pump.open();
    }
}

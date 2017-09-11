package com.tdl.study.tools.io.pump;

import com.tdl.study.core.io.pump.Pump;
import com.tdl.study.hadoop.hdfs.io.output.ParquetOutput;
import com.tdl.study.tools.io.pump.input.HDFSCsvInput;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.GroupFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ParquetFromCsv {

    public static void main(String[] args) throws IOException {

        if (args.length < 3) {
            System.out.println("Usage: " + ParquetFromCsv.class.getSimpleName() + " <schema.schema> <file.csv> <dest dir>");
            System.exit(1);
        }
        String schema = new BufferedReader(new FileReader(args[0])).lines().reduce((l1, l2) -> l1 + l2).orElse("");
        String csv = args[1];
        HDFSCsvInput input = new HDFSCsvInput(csv);
        ParquetOutput output = new ParquetOutput(schema, args[2],
                csv.replaceAll("^/", "").replaceAll("csv$", "parquet"));

        GroupFactory factory = output.groupFactory();

        Pump<Group> pump = Pump.pump(input.then(record -> {
            Group sg = factory.newGroup();
            record.forEach(key -> sg.append(key, record.get(key)));
            return sg;
        }), 8, output);
        pump.open();
    }
}

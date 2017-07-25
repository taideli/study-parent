package com.tdl.study.hdfs.io.pump;

import com.tdl.study.core.io.pump.Pump;
import com.tdl.study.hdfs.io.pump.input.HDFSCsvInput;
import com.tdl.study.hdfs.io.pump.output.ParquetOutput;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.GroupFactory;

import java.io.IOException;

public class ParquetFromCsv {

    public static void main(String[] args) throws IOException {
        String schema = "";
        String csv = "/taxi+_zone_lookup.csv";
        HDFSCsvInput input = new HDFSCsvInput(csv);
        ParquetOutput output = new ParquetOutput(schema, "/newyork_trip_data",
                csv.replaceAll("^/", ""));

        GroupFactory factory = output.groupFactory();

        Pump<Group> pump = Pump.pump(input.then(record -> {
            Group sg = factory.newGroup();
            record.forEach(key -> sg.append(key, record.get(key)));
            return sg;
        }), 8, output);
        pump.open();
    }
}

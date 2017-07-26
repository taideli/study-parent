package com.tdl.study.tools.io.pump.input;

import com.tdl.study.core.io.input.InputImpl;
import com.tdl.study.tools.io.pump.CSVRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * from
 */
public class HDFSCsvInput extends InputImpl<CSVRecord> {

    private AtomicInteger ptr = new AtomicInteger(0);
    private List<CSVRecord> records;

    public HDFSCsvInput(String path) throws IOException {
        super();
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Reader reader = new InputStreamReader(hdfs.open(new Path(path)));
        records = null;//CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader).getRecords();
        reader.close();
        hdfs.close();
        open();
    }

    @Override
    protected CSVRecord dequeue() {
        ptr.getAndIncrement();
        return records.remove(0);
    }

    @Override
    public boolean empty() {
        return records.size() > 0;
    }
}

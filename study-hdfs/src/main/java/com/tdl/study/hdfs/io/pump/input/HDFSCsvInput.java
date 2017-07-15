package com.tdl.study.hdfs.io.pump.input;

import com.tdl.study.core.io.input.InputImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * from
 */
public class HDFSCsvInput extends InputImpl<CSVRecord> {

    private FileSystem hdfs;
    private Reader reader;
    private CSVParser parser = null;

    public HDFSCsvInput(String path) {
        super();
        Configuration conf = new Configuration();
        try {
            hdfs = FileSystem.get(conf);
            reader = new InputStreamReader(hdfs.open(new Path(path)));
            parser = new CSVParser(reader, CSVFormat.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Parse CSV from hdfs, ", e);
        }
        open();
    }

    /**
     * csv header map
     * @return a copy of csv header map
     */
    public Map<String, Integer> getHeaderMap() {
        return parser.getHeaderMap();
    }

    @Override
    protected CSVRecord dequeue() {
        return parser.iterator().hasNext() ? parser.iterator().next() : null;
    }

    @Override
    public void close() {
        try {
            reader.close();
            parser.close();
            hdfs.close();
        } catch (IOException e) {
            logger().error("failed to close, for ", e);
        }
        super.close();
    }
}

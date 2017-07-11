package com.tdl.study.hbase.io;

import com.tdl.study.core.io.input.InputImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * from
 */
public class CsvInput extends InputImpl<CSVRecord> {

    private String path;
    private CSVParser parser = null;

    public CsvInput(String path) {
        super();
        this.path = path;
    }

    @Override
    public void open() {
        super.open();
        try {
            parser = CSVParser.parse(new File(this.path), Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return null;
    }

}

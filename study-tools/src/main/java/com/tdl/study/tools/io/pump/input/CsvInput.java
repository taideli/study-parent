/**
 * Created by Taideli on 2017/7/15.
 */
package com.tdl.study.tools.io.pump.input;

import com.tdl.study.core.io.input.InputImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * dequeue CSVRecord from local FileSystem
 */
public class CsvInput extends InputImpl<CSVRecord> {
    private CSVParser parser;

    public CsvInput(String path) {
        super();
        try {
            parser = CSVParser.parse(new File(path), Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("failed to parse csv from file [" + path + "], for ", e);
        }
        open();
    }

    @Override
    protected CSVRecord dequeue() {
        return parser.iterator().next();
    }

    @Override
    public boolean empty() {
        return !parser.iterator().hasNext();
    }

    @Override
    public void close() {
        try {
            parser.close();
        } catch (IOException ignored) {}
        super.close();
    }
}

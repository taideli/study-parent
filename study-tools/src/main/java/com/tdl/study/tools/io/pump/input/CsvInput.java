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
    CSVParser parser;

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
        System.out.println("into dequeue");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return parser.iterator().hasNext() ? parser.iterator().next() : null;
    }

    @Override
    public void close() {
        try {
            parser.close();
        } catch (IOException ignored) {}
        super.close();
    }
}

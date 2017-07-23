/**
 * Created by Taideli on 2017/7/15.
 */
package com.tdl.study.tools.io.pump.input;

import com.tdl.study.core.io.input.InputImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * dequeue CSVRecord from local FileSystem
 */
public class CsvInput extends InputImpl<CSVRecord> {
    private List<CSVRecord> records;
    private final int size;
    private AtomicInteger ptr = new AtomicInteger(0);

    public CsvInput(String path) {
        super();
        try {
            records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new FileReader(path)).getRecords();
            size = records.size();
        } catch (IOException e) {
            throw new RuntimeException("failed to parse csv from file [" + path + "], for ", e);
        }
        open();
    }

    @Override
    protected CSVRecord dequeue() {
        ptr.getAndIncrement();
        return records.remove(0);
    }

        @Override
    public boolean empty() {
        return ptr.get() >= size;
    }

    @Override
    public void close() {
        /*try {
            parser.close();
        } catch (IOException ignored) {}*/
        super.close();

        System.out.println("--size: " + size);
    }
}

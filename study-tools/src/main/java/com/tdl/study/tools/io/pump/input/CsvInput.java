/**
 * Created by Taideli on 2017/7/15.
 */
package com.tdl.study.tools.io.pump.input;

import com.tdl.study.core.io.input.InputImpl;
import com.tdl.study.tools.io.pump.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * dequeue CSVRecord from local FileSystem
 */
public class CsvInput extends InputImpl<CSVRecord> {
    private BufferedReader reader;
    private String delimiter;
    private Map<String, Integer> header = new HashMap<>();
    private AtomicBoolean end = new AtomicBoolean(false);
    private AtomicLong currentLine = new AtomicLong(0);
    private boolean firstLineAsHeader;

    public CsvInput(String path) throws IOException {
        this(path, true);
    }

    public CsvInput(String path, char delimiter) throws IOException {
        this(path, delimiter, true);
    }

    public CsvInput(String path, boolean firstLineAsHeader) throws IOException {
        this(path, ',', firstLineAsHeader);
    }

    public CsvInput(String path, char delimiter, boolean firstLineAsHeader) throws IOException {
        super();
        this.delimiter = String.valueOf(delimiter);
        reader = new BufferedReader(new FileReader(path));
        this.firstLineAsHeader = firstLineAsHeader;
        if (this.firstLineAsHeader) {
            String[] fieldNames = reader.readLine().split(this.delimiter);
            for (int i = 0; i < fieldNames.length; i++) {
                header.put(fieldNames[i], i);
            }
        }
        open();
    }

    public CsvInput withHeader(String... fields) {
        if (firstLineAsHeader) throw new RuntimeException("Csv file is defined as header");
        for (int i = 0; i < fields.length; i++) {
            header.put(fields[i], i);
        }
        return this;
    }

    @Override
    protected CSVRecord dequeue() {
        try {
            String line = reader.readLine();
            if (null == line) {
                end.set(true);
                return null;
            }
            long lineNumber = currentLine.incrementAndGet();
            return new CSVRecord(line.split(delimiter), header, lineNumber);
        } catch (IOException e) {
            end.set(true);
        }

        return null;
    }

    @Override
    public boolean empty() {
        return end.get();
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException ignored) {}
        super.close();

        System.out.println(getClass().getSimpleName() + " dequeue [" + currentLine.get() + "] items.");
    }
}

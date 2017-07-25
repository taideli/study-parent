package com.tdl.study.hdfs.io.pump.input;

import com.tdl.study.core.io.input.InputImpl;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CSVInput extends InputImpl<CSVInput.Record> {
    private Map<String, Integer> header = new HashMap<>();
    private boolean firstLineAsHeader = false;
    private String delimiter = null;
    private BufferedReader reader = null;
    private AtomicInteger currentLine = new AtomicInteger(0);

    public CSVInput(String filename) throws IOException {
        this(filename, ',', true);
    }

    public CSVInput(String filename, boolean firstLineAsHeader) throws IOException {
        this(filename, ',', firstLineAsHeader);
    }

    public CSVInput(String filename, char delimiter) throws IOException {
        this(filename, delimiter, true);
    }

    public CSVInput(String filename, char delimiter, boolean firstLineAsHeader) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        reader = new BufferedReader(new InputStreamReader(fs.open(new Path(filename))));
        if (firstLineAsHeader) {
            this.firstLineAsHeader = true;
            this.delimiter = String.valueOf(delimiter);
            String[] fields = reader.readLine().split(this.delimiter);
            for (int i = 0; i < fields.length; i++) {
                header.put(fields[i], i);
            }
        }
    }

    public CSVInput withHeader(String... fields) {
        if (firstLineAsHeader) throw new RuntimeException("Csv file is defined as header");
        for (int i = 0; i < fields.length; i++) {
            header.put(fields[i], i);
        }
        return this;
    }

    @Override
    protected Record dequeue() {
        // TODO: 2017/7/23 use reader.lines instead of readline
        try {
            String[] values = reader.readLine().split(delimiter, -1);
            currentLine.incrementAndGet();
            return new Record(values, currentLine.incrementAndGet(), header);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException ignored) {} finally {
            super.close();
        }
    }

    public class Record {
        private String[] values;
        private int lineNumber;
        private Map<String, Integer> header;

        public Record(String[] values, int lineNumber, Map<String, Integer> header) {
            this.values = values;
            this.lineNumber = lineNumber;
            this.header = header;
        }

        public String get(int index) {
            if (index < 0 || index >= values.length) throw new IndexOutOfBoundsException("Record filed size: " + values.length + " while index: " + index);
            return values[index];
        }

        public String get(String field) {
            int index = header.get(field);
            return values[index];
        }

        public int getLineNumber() {
            return lineNumber;
        }

        @Override
        public String toString() {
            return "line [" + lineNumber + "], values [" + Arrays.toString(values) + "]";
        }
    }
}

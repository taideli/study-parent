package com.tdl.study.hbase.io;

import com.tdl.study.core.io.Input;
import com.tdl.study.core.io.InputImpl;
import org.apache.commons.csv.CSVRecord;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * from
 */
public class CsvInput extends InputImpl<CSVRecord> {
    @Override
    protected CSVRecord dequeue() {
        return null;
    }
}

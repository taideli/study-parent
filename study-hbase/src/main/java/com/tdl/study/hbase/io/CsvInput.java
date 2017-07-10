package com.tdl.study.hbase.io;

import com.tdl.study.core.io.InputImpl;
import org.apache.commons.csv.CSVRecord;

/**
 * from
 */
public class CsvInput extends InputImpl<CSVRecord> {
    @Override
    protected CSVRecord dequeue() {
        return null;
    }
}

package com.tdl.study.tools.io.pump;

import java.util.*;

public class CSVRecord implements Iterable<String> {
    private String[] values;
    private Map<String, Integer> header;
    private long recordNumber;

    public CSVRecord(String[] values, Map<String, Integer> header, long recordNumber) {
        this.values = values;
        this.header = header;
        this.recordNumber = recordNumber;
    }

    public long getRecordNumber() {
        return recordNumber;
    }

    public String get(int index) {
        if (index < 0 || index >= values.length)
            throw new IndexOutOfBoundsException("CSVRecord values size: " + values.length + " while index: " + index);
        return values[index];
    }

    public String get(String field) {
        Integer index = header.get(field);
        if (null == index)
            throw new RuntimeException("CSVRecord header has no field \"" + field + "\", fields: "
                + Arrays.toString(header.keySet().toArray()));
        return values[index];
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        header.forEach((k, v) -> {
            map.put(k, values[v]);
        });

        return map;
    }

    private List<String> toList() {
        return Arrays.asList(values);
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    @Override
    public Iterator<String> iterator() {
        return toList().iterator();
    }
}

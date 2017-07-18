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
            records = CSVFormat.DEFAULT.parse(new FileReader(path)).getRecords();
            size = records.size();
        } catch (IOException e) {
            throw new RuntimeException("failed to parse csv from file [" + path + "], for ", e);
        }
        open();
    }

    /* logs:
     * CSVRecord [comment=null, mapping=null, recordNumber=1, values=[VendorID, tpep_pickup_datetime, tpep_dropoff_datetime, passenger_count, trip_distance, pickup_longitude, pickup_latitude, RatecodeID, store_and_fwd_flag, dropoff_longitude, dropoff_latitude, payment_type, fare_amount, extra, mta_tax, tip_amount, tolls_amount, improvement_surcharge, total_amount]]
     * CSVRecord [comment=null, mapping=null, recordNumber=2, values=[2, 2016-01-01 00:00:00, 2016-01-01 00:00:00, 2, 1.10, -73.990371704101563, 40.734695434570313, 1, N, -73.981842041015625, 40.732406616210937, 2, 7.5, 0.5, 0.5, 0, 0, 0.3, 8.8]]
     * CSVRecord [comment=null, mapping=null, recordNumber=1, values=[VendorID, tpep_pickup_datetime, tpep_dropoff_datetime, passenger_count, trip_distance, pickup_longitude, pickup_latitude, RatecodeID, store_and_fwd_flag, dropoff_longitude, dropoff_latitude, payment_type, fare_amount, extra, mta_tax, tip_amount, tolls_amount, improvement_surcharge, total_amount]]
     * CSVRecord [comment=null, mapping=null, recordNumber=4, values=[2, 2016-01-01 00:00:00, 2016-01-01 00:00:00, 1, 10.54, -73.984550476074219, 40.6795654296875, 1, N, -73.950271606445313, 40.788925170898438, 1, 33, 0.5, 0.5, 0, 0, 0.3, 34.3]]
     * CSVRecord [comment=null, mapping=null, recordNumber=4, values=[2, 2016-01-01 00:00:00, 2016-01-01 00:00:00, 1, 10.54, -73.984550476074219, 40.6795654296875, 1, N, -73.950271606445313, 40.788925170898438, 1, 33, 0.5, 0.5, 0, 0, 0.3, 34.3]]
     */
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

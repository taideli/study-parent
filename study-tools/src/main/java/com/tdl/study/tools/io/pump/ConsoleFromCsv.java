/**
 * Created by Taideli on 2017/7/15.
 */
package com.tdl.study.tools.io.pump;

import com.tdl.study.core.io.output.ConsoleOutput;
import com.tdl.study.core.io.pump.Pump;
import com.tdl.study.tools.io.pump.input.CsvInput;
import org.apache.commons.csv.CSVRecord;

public class ConsoleFromCsv {
    public static void main(String[] args) {

        CsvInput input = new CsvInput("G:\\迅雷下载\\_tripdata_2015-2016\\fhv_tripdata_2015-01.csv");
        ConsoleOutput output = new ConsoleOutput();
        Pump<String> pump = Pump
                .pump(input.then(CSVRecord::toString), 9, output)
                .batch(23);
        pump.open();
    }
}

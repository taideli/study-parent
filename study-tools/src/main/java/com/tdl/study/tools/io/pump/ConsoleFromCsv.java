/**
 * Created by Taideli on 2017/7/15.
 */
package com.tdl.study.tools.io.pump;

import com.tdl.study.core.io.output.ConsoleOutput;
import com.tdl.study.core.io.pump.Pump;
import com.tdl.study.tools.io.pump.input.CsvInput;

import java.io.IOException;

public class ConsoleFromCsv {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: " + ConsoleFromCsv.class.getSimpleName() + " <file.csv>");
            System.exit(1);
        }
        CsvInput input = new CsvInput(args[0]);
        ConsoleOutput output = new ConsoleOutput();
        Pump<String> pump = Pump
                .pump(input.then(CSVRecord::toString), 9, output)
                .batch(23);
        pump.open();
    }
}

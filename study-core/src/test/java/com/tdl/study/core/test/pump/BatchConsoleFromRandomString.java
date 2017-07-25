package com.tdl.study.core.test.pump;

import com.tdl.study.core.io.input.RandomStringInput;
import com.tdl.study.core.io.output.BatchConsoleOutput;
import com.tdl.study.core.io.pump.Pump;

public class BatchConsoleFromRandomString {

    public static void main(String[] args) {

        RandomStringInput input = new RandomStringInput(3478393);
        BatchConsoleOutput output = new BatchConsoleOutput();
        output.batchsize(4543);
        Pump<String> pump = Pump.pump(input, 7, output.prior(s -> "11---->" + s));
        pump.batch(3072).open();
    }
}

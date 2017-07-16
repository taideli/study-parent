package com.tdl.study.core.test.pump;

import com.tdl.study.core.io.pump.Pump;
import com.tdl.study.core.io.input.RandomStringInput;
import com.tdl.study.core.io.output.ConsoleOutput;

public class ConsoleFromRandomString {

    public static void main(String[] args) {

        RandomStringInput input = new RandomStringInput(159867);
        ConsoleOutput output = new ConsoleOutput();
        Pump<String> pump = Pump.pump(input, 7, output.prior(s -> "11---->" + s));
        pump.batch(3072).open();
    }
}

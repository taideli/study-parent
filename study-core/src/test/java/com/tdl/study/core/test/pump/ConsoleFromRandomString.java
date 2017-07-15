package com.tdl.study.core.test.pump;

import com.tdl.study.core.io.pump.Pump;

public class ConsoleFromRandomString {

    public static void main(String[] args) {

        RandomStringInput input = new RandomStringInput(100);
        ConsoleOutput output = new ConsoleOutput();
        Pump<String> pump = Pump.pump(input, 6, output.prior(s -> "11---->" + s));
        pump.batch(10).open();
    }
}

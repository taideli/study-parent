package com.tdl.study.core.test.pump;

import com.tdl.study.core.io.pump.Pump;

public class ConsoleFromUUID {

    public static void main(String[] args) {

//        UuidInput input = new UuidInput();
        StringInput input = new StringInput();
        ConsoleOutput output = new ConsoleOutput();
//        Pump<UUID> pump = Pump.pump(input, 6, output.prior(UUID::toString));
        Pump<String> pump = Pump.pump(input, 6, output);
        pump.batch(10).open();
    }
}
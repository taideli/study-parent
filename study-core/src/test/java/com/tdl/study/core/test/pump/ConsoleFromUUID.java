package com.tdl.study.core.test.pump;

import com.tdl.study.core.io.pump.Pump;

import java.util.UUID;

public class ConsoleFromUUID {

    public static void main(String[] args) {

        UuidInput input = new UuidInput(100);
        ConsoleOutput output = new ConsoleOutput();
        Pump<UUID> pump = Pump.pump(input, 6, output.prior(UUID::toString));
        pump.batch(1).open();
    }
}

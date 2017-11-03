package com.tdl.study.kafka.test;

import com.tdl.study.core.io.output.ConsoleOutput;
import com.tdl.study.core.io.pump.Pump;
import com.tdl.study.core.utils.URIs;
import com.tdl.study.kafka.io.KafkaInput;

public class KafkaInputTest {
    public static void main(String[] args) {
        URIs uri = URIs.builder()
                .schema("kafka")
                .username("qaz")
                .host("172.16.16.232", 9092)
                .parameter("group.id", "xxx")
                .build();

        KafkaInput input = new KafkaInput(uri);
        ConsoleOutput output = new ConsoleOutput();

        Pump pump = Pump.pump(input.then(records -> records.toString()), 2, output);

        pump.open();
    }
}

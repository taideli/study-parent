package com.tdl.study.kafka.test;

import com.tdl.study.core.io.input.RandomStringInput;
import com.tdl.study.core.io.pump.Pump;
import com.tdl.study.core.utils.URIs;
import com.tdl.study.kafka.io.KafkaOutput;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KafkaOutputTest {
    private static final Logger logger = LoggerFactory.getLogger(KafkaOutputTest.class);

    public static void main(String[] args) {
        URIs kafka = URIs.builder()
                .schema("kafka")
//                .username("tdl-topic-3")
                .username("qaz")
                .password("qaz")
                .host("172.16.16.232", 9092)
//                .parameter(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConfig.DEFAULT_KEY_SERIALIZER_CLASS)
//                .parameter(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConfig.DEFAULT_VALUE_SERIALIZER_CLASS)
                .build();

        RandomStringInput input = new RandomStringInput(100000);
        KafkaOutput output = new KafkaOutput(kafka);

        String topic = kafka.getUsername();
        Pump pump = Pump.pump(input/*.then(s -> {
            System.out.println(s);
            return s;
        })*/.then(s -> {
            ProducerRecord<byte[], byte[]> record = new ProducerRecord<byte[], byte[]>(topic, s.getBytes());
            return record;
        }), 3, output);
        logger.info(pump.name() + " start at " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(new Date()));
        pump.open();

        logger.info(pump.name() + " close at " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(new Date()));
    }
}

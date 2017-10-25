package com.tdl.study.kafka.test.config;

import com.tdl.study.core.utils.URIs;
import com.tdl.study.kafka.io.KafkaConfig;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class ConfigTest {
    public static void main(String[] args) {

        URIs uri = URIs.builder()
                .schema("kafka")
                .host("192.168.1.100", null)
                .host("192.168.1.101", 9092)
                .host("192.168.1.102", null)
                .parameter(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "123.456.789.12:111,123.456.789.12:222,123.456.789.12:333")
                .parameter(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true)
                .parameter(ProducerConfig.ACKS_CONFIG, true)
                .build();
        Properties properties = KafkaConfig.getProducerProperties(uri);
        properties.forEach((k, v) -> {
            System.out.println(k + " -> " + v);
        });
        System.out.println("====================================");
        properties = KafkaConfig.getConsumerProperties(uri);
        properties.forEach((k, v) -> {
            System.out.println(k + " -> " + v);
        });
    }
}

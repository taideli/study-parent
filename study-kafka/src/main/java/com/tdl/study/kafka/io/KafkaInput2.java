package com.tdl.study.kafka.io;

import com.tdl.study.core.utils.URIs;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;

public class KafkaInput2 {
    private ConsumerConnector connector;


    public KafkaInput2(String name, URIs uri) {
//        super(name);
        ConsumerConfig config = new ConsumerConfig(KafkaConfig.getConsumerProperties(uri));
        Consumer.createJavaConsumerConnector(config);
    }


}

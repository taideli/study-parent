package com.tdl.study.kafka.io;

import com.tdl.study.core.utils.URIs;
import kafka.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Predicate;

public class KafkaConfig {
    private URIs uri;
    private Map<String, String> query;

    public KafkaConfig(URIs uri) {
        this.uri = uri;
        query = uri.getParameters();
    }


    public Properties getConsumerConfig() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, )
        return properties;
    }

    public Properties getProducerCOnfig() {
        return new Properties();
    }

    private static List<String> getStaticFieldsValue(Class<?> clazz, Predicate<String> predicate) {
        if (null == clazz) return new ArrayList<>();

    }
}

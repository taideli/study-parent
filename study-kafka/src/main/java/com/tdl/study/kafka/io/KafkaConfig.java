package com.tdl.study.kafka.io;

import com.tdl.study.core.utils.URIs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.Serializer;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class that used to get standard kafka config properties from {@link URIs} query
 */
public final class KafkaConfig {
    private static final List<String> PRODUCER_CONFIG_KEYS = getConfigKeys(ProducerConfig.class);
    private static final List<String> CONSUMER_CONFIG_KEYS = getConfigKeys(ConsumerConfig.class);

    public static final Class<? extends Serializer> DEFAULT_KEY_SERIALIZER_CLASS = ByteArraySerializer.class;
    public static final Class<? extends Serializer> DEFAULT_VALUE_SERIALIZER_CLASS = ByteArraySerializer.class;
    public static final int DEFAULT_RETRIES_CONFIG = 3;

    /**
     * fetch producer properties from {@link URIs} query, the standard key defined at {@link ProducerConfig} <br/>
     * <br/>the key {@link ProducerConfig#BOOTSTRAP_SERVERS_CONFIG} fetch from uri's host
     * <br/>the default {@link ProducerConfig#KEY_SERIALIZER_CLASS_CONFIG} is {@link ByteArraySerializer}
     * <br/>the default {@link ProducerConfig#VALUE_SERIALIZER_CLASS_CONFIG} is {@link ByteArraySerializer}
     * <br/>the default {@link ProducerConfig#RETRIES_CONFIG} is {@link ByteArraySerializer}
     * @param uri the uri as source
     * @return properties with standard key and value form uris query
     */
    public static Properties getProducerProperties(URIs uri) {
        Properties properties = new Properties();
        uri.getParameters().forEach((k, v) -> {
            if (PRODUCER_CONFIG_KEYS.contains(k)) properties.put(k, v);
        });
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, uri.getHostsAsString());
        properties.putIfAbsent(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, DEFAULT_KEY_SERIALIZER_CLASS);
        properties.putIfAbsent(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DEFAULT_VALUE_SERIALIZER_CLASS);
        properties.putIfAbsent(ProducerConfig.RETRIES_CONFIG, DEFAULT_RETRIES_CONFIG);
        return properties;
    }

    /**
     * fetch consumer properties from {@link URIs} query, the standard key defined at {@link ConsumerConfig} <br/>
     * the key {@link ConsumerConfig#BOOTSTRAP_SERVERS_CONFIG} fetch from uri's host
     * @param uri the uri as source
     * @return properties with standard key and value form uris query
     */
    public static Properties getConsumerProperties(URIs uri) {
        Properties properties = new Properties();
        uri.getParameters().forEach((k, v) -> {
            if (CONSUMER_CONFIG_KEYS.contains(k)) properties.put(k, v);
        });
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, uri.getHostsAsString());
        return properties;
    }

    /**
     * get ConfigKey name using Java reflect
     * @param clazz config key to get from
     * @return key list
     */
    private static List<String> getConfigKeys(Class<? extends AbstractConfig> clazz) {
        if (null == clazz) return new ArrayList<>();
        return Stream.of(clazz.getDeclaredFields())
                .filter(field -> {
                    int modifier = field.getModifiers();
                    return  Modifier.isFinal(modifier)
                            && Modifier.isStatic(modifier)
                            && Modifier.isPublic(modifier)
                            && field.getGenericType().getTypeName().equals(String.class.getName());
                })
                .map(field -> {
                    try {
                        return field.get(clazz).toString();
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to get " + field.getName() + "'s static value from " + clazz.getSimpleName(), e);
                    }
                })
                .collect(Collectors.toList());
    }
}

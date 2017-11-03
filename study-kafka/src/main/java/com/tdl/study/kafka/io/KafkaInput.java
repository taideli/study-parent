package com.tdl.study.kafka.io;

import com.tdl.study.core.io.input.InputImpl;
import com.tdl.study.core.utils.Threads;
import com.tdl.study.core.utils.URIs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * kafka://topic@host1:port1[,host2:port2]?k1=v1[&k2=v2...]
 */
public class KafkaInput extends InputImpl<ConsumerRecords<byte[], byte[]>> {

    private Consumer<byte[], byte[]> consumer;
    private final long pollTimeout;

    public KafkaInput(String uri) {
        this(new URIs(uri));
    }

    public KafkaInput(URIs uri) {
        this(KafkaInput.class.getSimpleName() + "(" +  uri.getUsername() + ")", uri);
    }

    public KafkaInput(String name, String uri) {
        this(name, new URIs(uri));
    }

    public KafkaInput(String name, URIs uri) {
        super(name);

        if (!uri.getSchema().equals("kafka"))
            throw new RuntimeException(getClass().getSimpleName() + " NOT support schema " + uri.getSchema());
        pollTimeout = Long.parseLong(uri.getParameter("poll.timeout.ms", "100"));

        Properties properties = KafkaConfig.getConsumerProperties(uri);

        consumer = new KafkaConsumer<byte[], byte[]>(properties);
        consumer.subscribe(Collections.singletonList(uri.getUsername()));
//        consumer.subscribe(uri.getUsername());
        closing(consumer::close);

        logger().info("{} start subscribe {} with group {} at {}.", getClass().getSimpleName(), uri.getUsername(),
                properties.getProperty(ConsumerConfig.GROUP_ID_CONFIG), System.currentTimeMillis());
        open();
    }

    @Override
    protected ConsumerRecords<byte[], byte[]> dequeue() {
        Threads.sleep(2, TimeUnit.SECONDS);
        return consumer.poll(pollTimeout);
    }
}

package com.tdl.study.kafka.io;

import com.tdl.study.core.io.output.OutputImpl;
import com.tdl.study.core.utils.URIs;
import org.apache.kafka.clients.producer.*;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class KafkaOutput extends OutputImpl<ProducerRecord<byte[], byte[]>> {
    private Producer<byte[], byte[]> producer;

    public KafkaOutput(String name, String uri) {
        this(name, new URIs(uri));
    }

    public KafkaOutput(String name, URIs uri) {
        Properties properties = KafkaConfig.getProducerProperties(uri);
        producer = new KafkaProducer<byte[], byte[]>(properties);
        closing(() -> producer.close());
        open();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

    }

    public static void test() {
        Properties kafkaProps = new Properties();
//        kafkaProps.put("bootstrap.servers", "172.16.16.232:9092,172.16.16.232:9093");
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.16.232:9092");
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaProps);

        producer.partitionsFor("tdl-topic-3").forEach(info -> {
            System.out.println(info.toString());
        });
//        ProducerRecord<String, String> record = new ProducerRecord<String, String>("tdl-topic-1", "percision products", "France");
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("tdl-topic-3", null, System.currentTimeMillis(), UUID.randomUUID().toString(),"France");
        try {
            // 同步方法
            RecordMetadata metadata = producer.send(record).get();
            long offset = metadata.offset();
            System.out.println("offset is : " + offset);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        producer.close();


    }

    @Override
    protected boolean enqueue(ProducerRecord<byte[], byte[]> item) {
        producer.send(item);
        return true;
    }
}

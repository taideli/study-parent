package com.tdl.study.kafka.test;

import com.tdl.study.core.log.Logger;
import com.tdl.study.core.utils.URIs;
import com.tdl.study.kafka.io.KafkaConfig;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * rebalance 的处理
 */
public class HandleRebalance implements ConsumerRebalanceListener {
    private static final Logger log = Logger.getLogger(HandleRebalance.class);

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        if (null == partitions || partitions.isEmpty()) return;
        log.info("assigned: " +
                partitions.stream().map(tp -> tp.toString()).collect(Collectors.toList()));
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        if (null == partitions || partitions.isEmpty()) return;
        log.error("Lost partitions in rebalance. Committing current offsets: " +
                partitions.stream().map(tp -> tp.toString()).collect(Collectors.toList()));
//        consumer.commitSync(CurrentOffsets);
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        URIs uri = URIs.parse("kafka://xxx");
        Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
        Properties properties = KafkaConfig.getConsumerProperties(uri);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Starting exit....");
            consumer.wakeup();
            /*try {
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }));
        try {
            consumer.subscribe(Arrays.asList("topic_1"), new HandleRebalance());
//            consumer.seekToBeginning();
//            consumer.seekToEnd();
//            consumer.seek();
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record);
                    currentOffsets.put(new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1, "no metadata"));
                }
                consumer.commitAsync(currentOffsets, null);
            }
        } catch (WakeupException we) {
            //ignore, we are closing
        } catch (Exception e) {
            log.error("Unexpected error", e);
        } finally {
            try {
                consumer.commitAsync(currentOffsets, null);
            } finally {
                consumer.close();
                log.info("Closed consumer and we are done");
            }
        }
    }
}

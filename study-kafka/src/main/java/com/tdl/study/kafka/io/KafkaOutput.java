package com.tdl.study.kafka.io;

import com.tdl.study.core.io.output.OutputImpl;
import com.tdl.study.core.utils.URIs;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaOutput extends OutputImpl<ProducerRecord<byte[], byte[]>> {
    private Producer<byte[], byte[]> producer;

    public KafkaOutput(String uri) {
        this(new URIs(uri));
    }

    public KafkaOutput(URIs uri) {
        this(KafkaOutput.class.getSimpleName() + "(" +  uri.getUsername() + ")", uri);
    }

    public KafkaOutput(String name, String uri) {
        this(name, new URIs(uri));
    }

    public KafkaOutput(String name, URIs uri) {
        super(name);
        if (!uri.getSchema().equals("kafka"))
            throw new RuntimeException(getClass().getSimpleName() + " NOT support schema " + uri.getSchema());
        Properties properties = KafkaConfig.getProducerProperties(uri);
        producer = new KafkaProducer<byte[], byte[]>(properties);
        closing(producer::close); /*closing(() -> producer.close());*/
        open();
    }

    @Override
    protected boolean enqueue(ProducerRecord<byte[], byte[]> item) {
        producer.send(item, ((metadata, exception) -> {
//            System.out.println(String.format("%s\t%d\t%d\t%s", metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp()));
//            System.out.println("exception: " + exception);
            /** see {@link Callback}*/
            if (null == metadata || null != exception) {
                logger().debug("Failed to send " + item.topic() + " a record with timestamp: " + metadata.timestamp());
                // TODO: 2017/11/2 move the item failover thread and send again
            }
//            if (exception instanceof InvalidTopicException || exception instanceof RecordTooLargeException || exception instanceof UnknownServerException)
        }));
        return true;
    }

}

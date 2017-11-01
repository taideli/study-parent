#!/bin/bash

# 消费者
./bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic qaz
# 生产者
./bin/kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic qaz
package com.jehoon.food.common.messaging.kafka.consumer;

import java.util.List;

public interface KafkaConsumer<T> {
    void receive(List<T> messages, List<String> keys, List<Integer> partitions, List<Long> offsets);
}
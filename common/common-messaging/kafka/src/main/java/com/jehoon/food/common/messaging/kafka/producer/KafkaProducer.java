package com.jehoon.food.common.messaging.kafka.producer;

import java.io.Serializable;
import java.util.function.BiConsumer;

import org.springframework.kafka.support.SendResult;

public interface KafkaProducer<K extends Serializable, V> {
    void send(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback);
}

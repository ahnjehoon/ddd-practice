package com.jehoon.food.kafka.producer;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.jehoon.food.kafka.exception.KafkaProducerException;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {
    public static final String SENDING_MESSAGE = "메시지={} 를 토픽={}으로 전송 중";
    public static final String ERROR_ON_KAFKA_PRODUCER = "키: {}, 메시지: {} 및 예외: {}로 인한 Kafka 프로듀서 오류";
    public static final String KAFKA_PRODUCER_EXCEPTION = "키: {} 및 메시지: {}로 인한 Kafka 프로듀서 오류";
    public static final String CLOSING_KAFKA_PRODUCER = "Kafka 프로듀서를 종료합니다!";

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public void send(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback) {
        log.info(SENDING_MESSAGE, message, topicName);
        try {
            CompletableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
            kafkaResultFuture.whenComplete(callback);
        } catch (KafkaException e) {
            log.error(ERROR_ON_KAFKA_PRODUCER, key, message, e.getMessage());
            throw new KafkaProducerException(String.format(KAFKA_PRODUCER_EXCEPTION, key, message));
        }
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info(CLOSING_KAFKA_PRODUCER);
            kafkaTemplate.destroy();
        }
    }
}

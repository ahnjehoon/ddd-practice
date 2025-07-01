package com.jehoon.food.kafka.util;

import java.util.function.BiConsumer;

import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaMessageHelper {

    public static final String KAFKA_SEND_ERROR_MESSAGE = "토픽 {}에 {} 메시지 {} 전송 중 오류 발생";
    public static final String KAFKA_SUCCESS_MESSAGE = "주문 ID: {}에 대한 Kafka 성공 응답 수신 - 토픽: {} 파티션: {} 오프셋: {} 타임스탬프: {}";

    public <K, V> BiConsumer<SendResult<K, V>, Throwable> getKafkaCallback(
            String topicName,
            V message,
            String key,
            String messageType) {
        return (metadata, exception) -> {
            if (exception != null) {
                log.error(KAFKA_SEND_ERROR_MESSAGE,
                        topicName,
                        messageType,
                        message.toString(),
                        exception);
            } else {
                log.info(KAFKA_SUCCESS_MESSAGE,
                        key,
                        metadata.getRecordMetadata().topic(),
                        metadata.getRecordMetadata().partition(),
                        metadata.getRecordMetadata().offset(),
                        metadata.getRecordMetadata().timestamp());
            }
        };
    }
}

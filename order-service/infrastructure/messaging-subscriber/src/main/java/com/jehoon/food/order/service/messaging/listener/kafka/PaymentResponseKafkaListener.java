package com.jehoon.food.order.service.messaging.listener.kafka;

import java.util.List;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.jehoon.food.kafka.consumer.KafkaConsumer;
import com.jehoon.food.kafka.model.order.avro.PaymentResponseAvroModel;
import com.jehoon.food.kafka.model.order.avro.PaymentStatus;
import com.jehoon.food.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.jehoon.food.order.service.messaging.mapper.ResponseMessageMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {
    public static final String PAYMENT_RESPONSES_RECEIVED = "키: {}, 파티션: {}, 오프셋: {}으로 {} 개의 결제 응답을 수신했습니다";
    public static final String PROCESSING_SUCCESSFUL_PAYMENT = "주문 ID: {}에 대한 성공적인 결제를 처리 중입니다";
    public static final String PROCESSING_UNSUCCESSFUL_PAYMENT = "주문 ID: {}에 대한 실패한 결제를 처리 중입니다";

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final ResponseMessageMapper responseMessageMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info(PAYMENT_RESPONSES_RECEIVED,
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(this::processPaymentResponse);
    }

    private void processPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
        PaymentStatus paymentStatus = paymentResponseAvroModel.getPaymentStatus();
        UUID orderId = paymentResponseAvroModel.getOrderId();

        if (PaymentStatus.COMPLETED == paymentStatus) {
            log.info(PROCESSING_SUCCESSFUL_PAYMENT, orderId);
            paymentResponseMessageListener.paymentCompleted(
                    responseMessageMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
        } else if (isPaymentUnsuccessful(paymentStatus)) {
            log.info(PROCESSING_UNSUCCESSFUL_PAYMENT, orderId);
            paymentResponseMessageListener.paymentCancelled(
                    responseMessageMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
        }
    }

    private boolean isPaymentUnsuccessful(PaymentStatus paymentStatus) {
        return PaymentStatus.CANCELLED == paymentStatus || PaymentStatus.FAILED == paymentStatus;
    }
}

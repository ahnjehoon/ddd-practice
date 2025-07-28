package com.jehoon.food.order.service.messaging.publisher.kafka;

import org.springframework.stereotype.Component;

import com.jehoon.food.common.messaging.kafka.model.PaymentRequestModel;
import com.jehoon.food.common.messaging.kafka.producer.KafkaProducer;
import com.jehoon.food.common.messaging.kafka.util.KafkaMessageHelper;
import com.jehoon.food.order.application.config.OrderServiceConfigData;
import com.jehoon.food.order.application.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.jehoon.food.order.domain.event.OrderCreatedEvent;
import com.jehoon.food.order.service.messaging.mapper.RequestMessageMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    public static final String RECEIVED_EVENT_MESSAGE = "주문 ID: {}에 대한 주문 생성 이벤트를 수신했습니다.";
    public static final String SUCCESS_MESSAGE = "주문 ID: {}에 대한 결제 요청 메시지가 카프카로 성공적으로 전송되었습니다.";
    public static final String ERROR_MESSAGE = "주문 ID: {}에 대한 결제 요청 메시지를 카프카로 전송하는 중 오류가 발생했습니다. 오류: {}";

    private final RequestMessageMapper requestMessageMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, PaymentRequestModel> kafkaProducer;
    private final KafkaMessageHelper orderKafkaMessageHelper;

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info(RECEIVED_EVENT_MESSAGE, orderId);

        try {
            PaymentRequestModel paymentRequestModel = requestMessageMapper
                    .orderCreatedEventToPaymentRequestModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestModel,
                    orderKafkaMessageHelper
                            .getKafkaCallback(orderServiceConfigData.getPaymentResponseTopicName(),
                                    paymentRequestModel,
                                    orderId,
                                    paymentRequestModel.getClass().getName()));

            log.info(SUCCESS_MESSAGE, paymentRequestModel.getOrderId());
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, orderId, e.getMessage());
        }
    }
}
package com.jehoon.food.order.service.messaging.publisher.kafka;

import org.springframework.stereotype.Component;

import com.jehoon.food.kafka.model.order.avro.RestaurantApprovalRequestAvroModel;
import com.jehoon.food.kafka.producer.KafkaProducer;
import com.jehoon.food.kafka.util.KafkaMessageHelper;
import com.jehoon.food.order.service.domain.config.OrderServiceConfigData;
import com.jehoon.food.order.service.domain.event.OrderPaidEvent;
import com.jehoon.food.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.jehoon.food.order.service.messaging.mapper.RequestMessageMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

    public static final String SUCCESS_MESSAGE = "주문 ID: {}에 대한 레스토랑 승인 요청 메시지가 카프카로 성공적으로 전송되었습니다.";
    public static final String ERROR_MESSAGE = "주문 ID: {}에 대한 레스토랑 승인 요청 메시지를 카프카로 전송하는 중 오류가 발생했습니다. 오류: {}";

    private final RequestMessageMapper requestMessageMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final KafkaMessageHelper orderKafkaMessageHelper;

    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();

        try {
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = requestMessageMapper
                    .orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    orderId,
                    restaurantApprovalRequestAvroModel,
                    orderKafkaMessageHelper
                            .getKafkaCallback(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                                    restaurantApprovalRequestAvroModel,
                                    orderId,
                                    restaurantApprovalRequestAvroModel.getClass().getName()));

            log.info(SUCCESS_MESSAGE, orderId);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, orderId, e.getMessage());
        }
    }
}

package com.jehoon.food.order.service.messaging.listener.kafka;

import java.util.List;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.jehoon.food.common.messaging.kafka.consumer.KafkaConsumer;
import com.jehoon.food.common.messaging.kafka.model.order.avro.OrderApprovalStatus;
import com.jehoon.food.common.messaging.kafka.model.order.avro.RestaurantApprovalResponseAvroModel;
import com.jehoon.food.order.application.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import com.jehoon.food.order.domain.entity.Order;
import com.jehoon.food.order.service.messaging.mapper.ResponseMessageMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {
    public static final String APPROVAL_RESPONSES_RECEIVED = "키: {}, 파티션: {}, 오프셋: {}으로 {} 개의 레스토랑 승인 응답을 수신했습니다";
    public static final String PROCESSING_APPROVED_ORDER = "주문 ID: {}에 대한 승인된 주문을 처리 중입니다";
    public static final String PROCESSING_REJECTED_ORDER = "주문 ID: {}에 대한 거부된 주문을 처리 중입니다. 실패 메시지: {}";

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final ResponseMessageMapper responseMessageMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}", topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info(APPROVAL_RESPONSES_RECEIVED,
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(this::processRestaurantApprovalResponse);
    }

    private void processRestaurantApprovalResponse(
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
        OrderApprovalStatus approvalStatus = restaurantApprovalResponseAvroModel.getOrderApprovalStatus();
        UUID orderId = restaurantApprovalResponseAvroModel.getOrderId();

        if (OrderApprovalStatus.APPROVED == approvalStatus) {
            log.info(PROCESSING_APPROVED_ORDER, orderId);
            restaurantApprovalResponseMessageListener.orderApproved(
                    responseMessageMapper
                            .approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel));
        } else if (OrderApprovalStatus.REJECTED == approvalStatus) {
            String failureMessages = String.join(
                    Order.FAILURE_MESSAGE_DELIMITER,
                    restaurantApprovalResponseAvroModel.getFailureMessages());

            log.info(PROCESSING_REJECTED_ORDER, orderId, failureMessages);
            restaurantApprovalResponseMessageListener.orderRejected(
                    responseMessageMapper
                            .approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel));
        }
    }

}

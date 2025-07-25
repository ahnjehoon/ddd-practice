package com.jehoon.food.order.service.messaging.mapper;

import org.springframework.stereotype.Component;

import com.jehoon.food.common.messaging.kafka.model.order.avro.PaymentResponseAvroModel;
import com.jehoon.food.common.messaging.kafka.model.order.avro.RestaurantApprovalResponseAvroModel;
import com.jehoon.food.order.application.dto.message.PaymentResponse;
import com.jehoon.food.order.application.dto.message.RestaurantApprovalResponse;

@Component
public class ResponseMessageMapper {

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(
            PaymentResponseAvroModel paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .paymentId(paymentResponseAvroModel.getPaymentId())
                .customerId(paymentResponseAvroModel.getCustomerId())
                .orderId(paymentResponseAvroModel.getOrderId())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(com.jehoon.food.domain.valueobject.PaymentStatus.valueOf(
                        paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
        return RestaurantApprovalResponse.builder()
                .id(restaurantApprovalResponseAvroModel.getId())
                .sagaId(restaurantApprovalResponseAvroModel.getSagaId())
                .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId())
                .orderId(restaurantApprovalResponseAvroModel.getOrderId())
                .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
                .orderApprovalStatus(
                        com.jehoon.food.domain.valueobject.OrderApprovalStatus.valueOf(
                                restaurantApprovalResponseAvroModel
                                        .getOrderApprovalStatus().name()))
                .failureMessages(restaurantApprovalResponseAvroModel.getFailureMessages())
                .build();
    }
}

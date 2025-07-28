package com.jehoon.food.order.service.messaging.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.jehoon.food.common.messaging.kafka.model.PaymentResponseModel;
import com.jehoon.food.common.messaging.kafka.model.RestaurantApprovalResponseModel;
import com.jehoon.food.order.application.dto.message.PaymentResponse;
import com.jehoon.food.order.application.dto.message.RestaurantApprovalResponse;

@Component
public class ResponseMessageMapper {

    public PaymentResponse paymentResponseModelToPaymentResponse(
            PaymentResponseModel paymentResponseModel) {
        return PaymentResponse.builder()
                .id(UUID.fromString(paymentResponseModel.getId()))
                .sagaId(UUID.fromString(paymentResponseModel.getSagaId()))
                .paymentId(UUID.fromString(paymentResponseModel.getPaymentId()))
                .customerId(UUID.fromString(paymentResponseModel.getCustomerId()))
                .orderId(UUID.fromString(paymentResponseModel.getOrderId()))
                .price(paymentResponseModel.getPrice())
                .createdAt(paymentResponseModel.getCreatedAt())
                .paymentStatus(com.jehoon.food.domain.valueobject.PaymentStatus.valueOf(
                        paymentResponseModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse approvalResponseModelToApprovalResponse(
            RestaurantApprovalResponseModel restaurantApprovalResponseModel) {
        return RestaurantApprovalResponse.builder()
                .id(UUID.fromString(restaurantApprovalResponseModel.getId()))
                .sagaId(UUID.fromString(restaurantApprovalResponseModel.getSagaId()))
                .restaurantId(UUID.fromString(restaurantApprovalResponseModel.getRestaurantId()))
                .orderId(UUID.fromString(restaurantApprovalResponseModel.getOrderId()))
                .createdAt(restaurantApprovalResponseModel.getCreatedAt())
                .orderApprovalStatus(
                        com.jehoon.food.domain.valueobject.OrderApprovalStatus.valueOf(
                                restaurantApprovalResponseModel
                                        .getOrderApprovalStatus().name()))
                .failureMessages(restaurantApprovalResponseModel.getFailureMessages())
                .build();
    }
}

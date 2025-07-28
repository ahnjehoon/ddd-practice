package com.jehoon.food.order.service.messaging.mapper;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jehoon.food.common.messaging.kafka.model.PaymentRequestModel;
import com.jehoon.food.common.messaging.kafka.model.RestaurantApprovalRequestModel;
import com.jehoon.food.order.domain.entity.Order;
import com.jehoon.food.order.domain.event.OrderCancelledEvent;
import com.jehoon.food.order.domain.event.OrderCreatedEvent;
import com.jehoon.food.order.domain.event.OrderPaidEvent;

@Component
public class RequestMessageMapper {

    public PaymentRequestModel orderCreatedEventToPaymentRequestModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestModel.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(UUID.randomUUID().toString())
                .customerId(order.getCustomerId().getValue().toString())
                .orderId(order.getId().getValue().toString())
                .price(order.getPrice().getAmount())
                .createdAt(orderCreatedEvent.getCreatedAt().toInstant())
                .paymentOrderStatus(PaymentRequestModel.PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestModel orderCancelledEventToPaymentRequestModel(
            OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();
        return PaymentRequestModel.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(UUID.randomUUID().toString())
                .customerId(order.getCustomerId().getValue().toString())
                .orderId(order.getId().getValue().toString())
                .price(order.getPrice().getAmount())
                .createdAt(orderCancelledEvent.getCreatedAt().toInstant())
                .paymentOrderStatus(PaymentRequestModel.PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestModel orderPaidEventToRestaurantApprovalRequestModel(
            OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestModel.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(UUID.randomUUID().toString())
                .orderId(order.getId().getValue().toString())
                .restaurantId(order.getRestaurantId().getValue().toString())
                .restaurantOrderStatus(RestaurantApprovalRequestModel.RestaurantOrderStatus.PAID)
                .products(order.getOrderItems().stream()
                        .map(orderItem -> RestaurantApprovalRequestModel.Product.builder()
                                .id(orderItem.getProduct().getId().getValue().toString())
                                .quantity(orderItem.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .price(order.getPrice().getAmount())
                .createdAt(orderPaidEvent.getCreatedAt().toInstant())
                .build();
    }
}
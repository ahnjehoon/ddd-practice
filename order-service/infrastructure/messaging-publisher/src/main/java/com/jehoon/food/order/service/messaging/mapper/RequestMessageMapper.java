package com.jehoon.food.order.service.messaging.mapper;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jehoon.food.common.messaging.kafka.model.order.avro.PaymentOrderStatus;
import com.jehoon.food.common.messaging.kafka.model.order.avro.PaymentRequestAvroModel;
import com.jehoon.food.common.messaging.kafka.model.order.avro.RestaurantApprovalRequestAvroModel;
import com.jehoon.food.order.domain.entity.Order;
import com.jehoon.food.order.domain.event.OrderCancelledEvent;
import com.jehoon.food.order.domain.event.OrderCreatedEvent;
import com.jehoon.food.order.domain.event.OrderPaidEvent;

@Component
public class RequestMessageMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(
            OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(
            OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setOrderId(order.getId().getValue())
                .setRestaurantId(order.getRestaurantId().getValue())
                .setRestaurantOrderStatus(
                        com.jehoon.food.common.messaging.kafka.model.order.avro.RestaurantOrderStatus
                                .valueOf(order.getOrderStatus().name()))
                .setProducts(order.getOrderItems().stream()
                        .map(orderItem -> com.jehoon.food.common.messaging.kafka.model.order.avro.Product
                                .newBuilder()
                                .setId(orderItem.getProduct().getId().getValue()
                                        .toString())
                                .setQuantity(orderItem.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .build();
    }
}
package com.jehoon.food.order.service.messaging.mapper;

import java.util.UUID;
import java.util.stream.Collectors;

import com.jehoon.food.kafka.model.order.avro.PaymentOrderStatus;
import com.jehoon.food.kafka.model.order.avro.PaymentRequestAvroModel;
import com.jehoon.food.kafka.model.order.avro.PaymentResponseAvroModel;
import com.jehoon.food.kafka.model.order.avro.RestaurantApprovalRequestAvroModel;
import com.jehoon.food.kafka.model.order.avro.RestaurantApprovalResponseAvroModel;
import com.jehoon.food.kafka.model.order.avro.RestaurantOrderStatus;
import com.jehoon.food.order.service.domain.dto.message.PaymentResponse;
import com.jehoon.food.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.jehoon.food.order.service.domain.entity.Order;
import com.jehoon.food.order.service.domain.event.OrderCancelledEvent;
import com.jehoon.food.order.service.domain.event.OrderCreatedEvent;
import com.jehoon.food.order.service.domain.event.OrderPaidEvent;

public class OrderMessagingDataMapper {

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
                                .setOrderId(order.getId().getValue())
                                .setRestaurantOrderStatus(com.jehoon.food.kafka.model.order.avro.RestaurantOrderStatus
                                                .valueOf(order.getOrderStatus().name()))
                                .setProducts(order.getOrderItems().stream()
                                                .map(orderItem -> com.jehoon.food.kafka.model.order.avro.Product
                                                                .newBuilder()
                                                                .setId(orderItem.getProduct().getId().getValue()
                                                                                .toString())
                                                                .setQuantity(orderItem.getQuantity())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .setPrice(order.getPrice().getAmount())
                                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                                .build();
        }

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

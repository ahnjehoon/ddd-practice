package com.jehoon.food.order.application.ports.output.message.publisher.payment;

import com.jehoon.food.domain.event.publisher.DomainEventPublisher;
import com.jehoon.food.order.domain.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}

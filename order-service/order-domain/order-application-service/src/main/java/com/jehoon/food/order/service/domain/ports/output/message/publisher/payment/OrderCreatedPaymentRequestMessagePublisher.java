package com.jehoon.food.order.service.domain.ports.output.message.publisher.payment;

import com.jehoon.food.domain.event.publisher.DomainEventPublisher;
import com.jehoon.food.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {
}

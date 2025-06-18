package com.jehoon.food.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.jehoon.food.domain.event.publisher.DomainEventPublisher;
import com.jehoon.food.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}

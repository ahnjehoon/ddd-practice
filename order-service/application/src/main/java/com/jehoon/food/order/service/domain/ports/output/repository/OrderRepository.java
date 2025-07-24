package com.jehoon.food.order.service.domain.ports.output.repository;

import java.util.Optional;

import com.jehoon.food.domain.valueobject.OrderId;
import com.jehoon.food.order.service.domain.entity.Order;
import com.jehoon.food.order.service.domain.valueobject.TrackingId;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(OrderId orderId);

    Optional<Order> findByTrackingId(TrackingId trackingId);
}

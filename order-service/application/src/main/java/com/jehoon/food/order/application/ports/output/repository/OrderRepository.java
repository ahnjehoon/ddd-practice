package com.jehoon.food.order.application.ports.output.repository;

import java.util.Optional;

import com.jehoon.food.domain.valueobject.OrderId;
import com.jehoon.food.order.domain.entity.Order;
import com.jehoon.food.order.domain.valueobject.TrackingId;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(OrderId orderId);

    Optional<Order> findByTrackingId(TrackingId trackingId);
}

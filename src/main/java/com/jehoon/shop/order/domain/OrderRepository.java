package com.jehoon.shop.order.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface OrderRepository extends Repository<Order, OrderNo> {
    Optional<Order> findById(OrderNo id);
}

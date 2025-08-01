package com.jehoon.food.order.service.dataaccess.order.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.jehoon.food.domain.valueobject.OrderId;
import com.jehoon.food.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.jehoon.food.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.jehoon.food.order.domain.entity.Order;
import com.jehoon.food.order.application.ports.output.repository.OrderRepository;
import com.jehoon.food.order.domain.valueobject.TrackingId;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(orderJpaRepository
                .save(orderDataAccessMapper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return orderJpaRepository.findById(orderId.getValue()).map(orderDataAccessMapper::orderEntityToOrder);
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }
}

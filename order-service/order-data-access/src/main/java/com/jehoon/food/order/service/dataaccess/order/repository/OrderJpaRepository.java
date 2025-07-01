package com.jehoon.food.order.service.dataaccess.order.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jehoon.food.order.service.dataaccess.order.entity.OrderEntity;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<OrderEntity> findByTrackingId(UUID trackingId);

}

package com.jehoon.food.order.service.dataaccess.customer.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jehoon.food.order.service.dataaccess.customer.entity.CustomerEntity;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

}

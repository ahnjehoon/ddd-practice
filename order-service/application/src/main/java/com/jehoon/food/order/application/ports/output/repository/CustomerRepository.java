package com.jehoon.food.order.application.ports.output.repository;

import java.util.Optional;
import java.util.UUID;

import com.jehoon.food.order.domain.entity.Customer;

public interface CustomerRepository {

    Optional<Customer> findCustomer(UUID customerId);
}

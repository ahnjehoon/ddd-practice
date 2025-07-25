package com.jehoon.food.order.service.dataaccess.customer.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.jehoon.food.order.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.jehoon.food.order.service.dataaccess.customer.repository.CustomerJpaRepository;
import com.jehoon.food.order.domain.entity.Customer;
import com.jehoon.food.order.application.ports.output.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJpaRepository.findById(customerId).map(customerDataAccessMapper::customerEntityToCustomer);
    }

}

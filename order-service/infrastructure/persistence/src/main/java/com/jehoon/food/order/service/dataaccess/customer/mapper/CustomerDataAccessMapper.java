package com.jehoon.food.order.service.dataaccess.customer.mapper;

import org.springframework.stereotype.Component;

import com.jehoon.food.domain.valueobject.CustomerId;
import com.jehoon.food.order.service.dataaccess.customer.entity.CustomerEntity;
import com.jehoon.food.order.domain.entity.Customer;

@Component
public class CustomerDataAccessMapper {
    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }
}

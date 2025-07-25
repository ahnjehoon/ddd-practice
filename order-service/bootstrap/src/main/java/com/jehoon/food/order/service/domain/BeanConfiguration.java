package com.jehoon.food.order.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jehoon.food.order.domain.OrderDomainService;
import com.jehoon.food.order.domain.OrderDomainServiceImpl;

@Configuration
public class BeanConfiguration {

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }
}

package com.jehoon.food.restaurant.service.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jehoon.food.restaurant.service.domain.RestaurantDomainService;
import com.jehoon.food.restaurant.service.domain.RestaurantDomainServiceImpl;

@Configuration
public class RestaurantServiceConfig {

    @Bean
    public RestaurantDomainService restaurantDomainService() {
        return new RestaurantDomainServiceImpl();
    }
}
package com.jehoon.food.restaurant.service.application.ports.output.repository;

import java.util.Optional;

import com.jehoon.food.domain.valueobject.RestaurantId;
import com.jehoon.food.restaurant.service.domain.Restaurant;

public interface RestaurantRepository {
    
    Restaurant save(Restaurant restaurant);
    
    Optional<Restaurant> findById(RestaurantId restaurantId);
    
    Optional<Restaurant> findByIdWithProducts(RestaurantId restaurantId);
}
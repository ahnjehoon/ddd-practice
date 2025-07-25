package com.jehoon.food.order.application.ports.output.repository;

import java.util.Optional;

import com.jehoon.food.order.domain.entity.Restaurant;

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}

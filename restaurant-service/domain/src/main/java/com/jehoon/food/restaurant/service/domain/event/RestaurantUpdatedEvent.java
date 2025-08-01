package com.jehoon.food.restaurant.service.domain.event;

import java.time.ZonedDateTime;

import com.jehoon.food.restaurant.service.domain.Restaurant;

public class RestaurantUpdatedEvent extends RestaurantEvent {
    public RestaurantUpdatedEvent(Restaurant restaurant, ZonedDateTime createdAt) {
        super(restaurant, createdAt);
    }
}
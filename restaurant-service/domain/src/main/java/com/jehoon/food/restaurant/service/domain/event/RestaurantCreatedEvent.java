package com.jehoon.food.restaurant.service.domain.event;

import java.time.ZonedDateTime;

import com.jehoon.food.restaurant.service.domain.Restaurant;

public class RestaurantCreatedEvent extends RestaurantEvent {
    public RestaurantCreatedEvent(Restaurant restaurant, ZonedDateTime createdAt) {
        super(restaurant, createdAt);
    }
}
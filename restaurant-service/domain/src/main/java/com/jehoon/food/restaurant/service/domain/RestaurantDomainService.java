package com.jehoon.food.restaurant.service.domain;

import java.util.List;

import com.jehoon.food.domain.valueobject.OrderId;
import com.jehoon.food.restaurant.service.domain.event.OrderApprovedEvent;
import com.jehoon.food.restaurant.service.domain.event.OrderRejectedEvent;
import com.jehoon.food.restaurant.service.domain.event.RestaurantCreatedEvent;
import com.jehoon.food.restaurant.service.domain.event.RestaurantUpdatedEvent;

public interface RestaurantDomainService {

    RestaurantCreatedEvent validateAndCreateRestaurant(Restaurant restaurant);

    RestaurantUpdatedEvent updateRestaurant(Restaurant restaurant);

    OrderApprovedEvent approveOrder(Restaurant restaurant, OrderId orderId, List<Product> products);

    OrderRejectedEvent rejectOrder(Restaurant restaurant, OrderId orderId, String rejectionReason);
}
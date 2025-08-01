package com.jehoon.food.restaurant.service.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.jehoon.food.domain.valueobject.OrderId;
import com.jehoon.food.restaurant.service.domain.event.OrderApprovedEvent;
import com.jehoon.food.restaurant.service.domain.event.OrderRejectedEvent;
import com.jehoon.food.restaurant.service.domain.event.RestaurantCreatedEvent;
import com.jehoon.food.restaurant.service.domain.event.RestaurantUpdatedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {

    @Override
    public RestaurantCreatedEvent validateAndCreateRestaurant(Restaurant restaurant) {
        log.info("Creating restaurant with id: {}", restaurant.getId().getValue());
        // 비즈니스 규칙 검증
        validateRestaurant(restaurant);
        
        return new RestaurantCreatedEvent(restaurant, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public RestaurantUpdatedEvent updateRestaurant(Restaurant restaurant) {
        log.info("Updating restaurant with id: {}", restaurant.getId().getValue());
        // 비즈니스 규칙 검증
        validateRestaurant(restaurant);
        
        return new RestaurantUpdatedEvent(restaurant, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public OrderApprovedEvent approveOrder(Restaurant restaurant, OrderId orderId, List<Product> products) {
        log.info("Approving order with id: {} for restaurant: {}", orderId.getValue(), restaurant.getId().getValue());
        
        // 레스토랑이 활성 상태인지 확인
        if (!restaurant.isActive()) {
            throw new RestaurantDomainException("Restaurant is not active!");
        }
        
        // 주문한 상품들이 모두 사용 가능한지 확인
        validateOrderProducts(restaurant, products);
        
        return new OrderApprovedEvent(restaurant, orderId, products, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public OrderRejectedEvent rejectOrder(Restaurant restaurant, OrderId orderId, String rejectionReason) {
        log.info("Rejecting order with id: {} for restaurant: {}, reason: {}", 
                orderId.getValue(), restaurant.getId().getValue(), rejectionReason);
        
        return new OrderRejectedEvent(restaurant, orderId, rejectionReason, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (restaurant.getName() == null || restaurant.getName().trim().isEmpty()) {
            throw new RestaurantDomainException("Restaurant name cannot be empty!");
        }
        if (restaurant.getProducts() == null || restaurant.getProducts().isEmpty()) {
            throw new RestaurantDomainException("Restaurant must have at least one product!");
        }
    }

    private void validateOrderProducts(Restaurant restaurant, List<Product> orderProducts) {
        List<Product> restaurantProducts = restaurant.getProducts();
        
        for (Product orderProduct : orderProducts) {
            Product restaurantProduct = restaurantProducts.stream()
                    .filter(p -> p.getId().equals(orderProduct.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RestaurantDomainException(
                            "Product with id: " + orderProduct.getId().getValue() + " not found in restaurant"));
            
            if (!restaurantProduct.isAvailable()) {
                throw new RestaurantDomainException(
                        "Product with id: " + orderProduct.getId().getValue() + " is not available");
            }
            
            if (restaurantProduct.getQuantity() < orderProduct.getQuantity()) {
                throw new RestaurantDomainException(
                        "Product with id: " + orderProduct.getId().getValue() + " has insufficient quantity");
            }
        }
    }
}
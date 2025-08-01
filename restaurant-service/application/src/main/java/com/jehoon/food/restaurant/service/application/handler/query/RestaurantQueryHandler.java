package com.jehoon.food.restaurant.service.application.handler.query;

import org.springframework.stereotype.Component;

import com.jehoon.food.domain.valueobject.RestaurantId;
import com.jehoon.food.restaurant.service.application.dto.query.FindRestaurantQuery;
import com.jehoon.food.restaurant.service.application.dto.query.FindRestaurantResponse;
import com.jehoon.food.restaurant.service.application.mapper.RestaurantDataMapper;
import com.jehoon.food.restaurant.service.application.ports.output.repository.RestaurantRepository;
import com.jehoon.food.restaurant.service.domain.Restaurant;
import com.jehoon.food.restaurant.service.domain.RestaurantDomainException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestaurantQueryHandler {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantDataMapper restaurantDataMapper;

    public RestaurantQueryHandler(RestaurantRepository restaurantRepository,
                                RestaurantDataMapper restaurantDataMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantDataMapper = restaurantDataMapper;
    }

    public FindRestaurantResponse findRestaurant(FindRestaurantQuery findRestaurantQuery) {
        log.info("Finding restaurant with id: {}", findRestaurantQuery.getRestaurantId());
        
        RestaurantId restaurantId = new RestaurantId(findRestaurantQuery.getRestaurantId());
        
        Restaurant restaurant = restaurantRepository.findByIdWithProducts(restaurantId)
                .orElseThrow(() -> new RestaurantDomainException(
                        "Restaurant with id " + restaurantId.getValue() + " not found"));
        
        log.info("Restaurant found with id: {}", restaurant.getId().getValue());
        
        return restaurantDataMapper.restaurantToFindRestaurantResponse(restaurant);
    }
}
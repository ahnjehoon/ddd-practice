package com.jehoon.food.order.service.dataaccess.restaurant.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.jehoon.food.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.jehoon.food.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.jehoon.food.order.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.jehoon.food.order.domain.entity.Restaurant;
import com.jehoon.food.order.application.ports.output.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(),
                        restaurantProducts);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}

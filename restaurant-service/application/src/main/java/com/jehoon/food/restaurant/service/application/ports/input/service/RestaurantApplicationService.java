package com.jehoon.food.restaurant.service.application.ports.input.service;

import com.jehoon.food.restaurant.service.application.dto.command.ApproveOrderCommand;
import com.jehoon.food.restaurant.service.application.dto.command.ApproveOrderResponse;
import com.jehoon.food.restaurant.service.application.dto.command.CreateRestaurantCommand;
import com.jehoon.food.restaurant.service.application.dto.command.CreateRestaurantResponse;
import com.jehoon.food.restaurant.service.application.dto.command.UpdateProductCommand;
import com.jehoon.food.restaurant.service.application.dto.command.UpdateProductResponse;
import com.jehoon.food.restaurant.service.application.dto.query.FindRestaurantQuery;
import com.jehoon.food.restaurant.service.application.dto.query.FindRestaurantResponse;

import javax.validation.Valid;

public interface RestaurantApplicationService {

    CreateRestaurantResponse createRestaurant(@Valid CreateRestaurantCommand createRestaurantCommand);

    UpdateProductResponse updateProduct(@Valid UpdateProductCommand updateProductCommand);

    ApproveOrderResponse approveOrder(@Valid ApproveOrderCommand approveOrderCommand);

    FindRestaurantResponse findRestaurant(@Valid FindRestaurantQuery findRestaurantQuery);
}
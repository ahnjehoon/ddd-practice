package com.jehoon.food.restaurant.service.application;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.jehoon.food.restaurant.service.application.dto.command.ApproveOrderCommand;
import com.jehoon.food.restaurant.service.application.dto.command.ApproveOrderResponse;
import com.jehoon.food.restaurant.service.application.dto.command.CreateRestaurantCommand;
import com.jehoon.food.restaurant.service.application.dto.command.CreateRestaurantResponse;
import com.jehoon.food.restaurant.service.application.dto.command.UpdateProductCommand;
import com.jehoon.food.restaurant.service.application.dto.command.UpdateProductResponse;
import com.jehoon.food.restaurant.service.application.dto.query.FindRestaurantQuery;
import com.jehoon.food.restaurant.service.application.dto.query.FindRestaurantResponse;
import com.jehoon.food.restaurant.service.application.handler.command.ApproveOrderCommandHandler;
import com.jehoon.food.restaurant.service.application.handler.command.CreateRestaurantCommandHandler;
import com.jehoon.food.restaurant.service.application.handler.query.RestaurantQueryHandler;
import com.jehoon.food.restaurant.service.application.ports.input.service.RestaurantApplicationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Service
public class RestaurantApplicationServiceImpl implements RestaurantApplicationService {

    private final CreateRestaurantCommandHandler createRestaurantCommandHandler;
    private final ApproveOrderCommandHandler approveOrderCommandHandler;
    private final RestaurantQueryHandler restaurantQueryHandler;

    public RestaurantApplicationServiceImpl(CreateRestaurantCommandHandler createRestaurantCommandHandler,
                                          ApproveOrderCommandHandler approveOrderCommandHandler,
                                          RestaurantQueryHandler restaurantQueryHandler) {
        this.createRestaurantCommandHandler = createRestaurantCommandHandler;
        this.approveOrderCommandHandler = approveOrderCommandHandler;
        this.restaurantQueryHandler = restaurantQueryHandler;
    }

    @Override
    public CreateRestaurantResponse createRestaurant(CreateRestaurantCommand createRestaurantCommand) {
        return createRestaurantCommandHandler.createRestaurant(createRestaurantCommand);
    }

    @Override
    public UpdateProductResponse updateProduct(UpdateProductCommand updateProductCommand) {
        // TODO: UpdateProductCommandHandler 구현
        throw new UnsupportedOperationException("UpdateProduct not implemented yet");
    }

    @Override
    public ApproveOrderResponse approveOrder(ApproveOrderCommand approveOrderCommand) {
        return approveOrderCommandHandler.approveOrder(approveOrderCommand);
    }

    @Override
    public FindRestaurantResponse findRestaurant(FindRestaurantQuery findRestaurantQuery) {
        return restaurantQueryHandler.findRestaurant(findRestaurantQuery);
    }
}
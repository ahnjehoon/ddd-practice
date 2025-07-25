package com.jehoon.food.order.application.ports.input.service;

import com.jehoon.food.order.application.dto.create.CreateOrderCommand;
import com.jehoon.food.order.application.dto.create.CreateOrderResponse;
import com.jehoon.food.order.application.dto.track.TrackOrderQuery;
import com.jehoon.food.order.application.dto.track.TrackOrderResponse;

import jakarta.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}

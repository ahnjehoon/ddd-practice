package com.jehoon.food.order.application;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.jehoon.food.order.application.dto.create.CreateOrderCommand;
import com.jehoon.food.order.application.dto.create.CreateOrderResponse;
import com.jehoon.food.order.application.dto.track.TrackOrderQuery;
import com.jehoon.food.order.application.dto.track.TrackOrderResponse;
import com.jehoon.food.order.application.ports.input.service.OrderApplicationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;
    private final OrderTrackCommandHandler orderTrackCommandHandler;

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}

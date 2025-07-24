package com.jehoon.food.order.service.domain;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.jehoon.food.order.service.domain.dto.create.CreateOrderCommand;
import com.jehoon.food.order.service.domain.dto.create.CreateOrderResponse;
import com.jehoon.food.order.service.domain.dto.track.TrackOrderQuery;
import com.jehoon.food.order.service.domain.dto.track.TrackOrderResponse;
import com.jehoon.food.order.service.domain.ports.input.service.OrderApplicationService;

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

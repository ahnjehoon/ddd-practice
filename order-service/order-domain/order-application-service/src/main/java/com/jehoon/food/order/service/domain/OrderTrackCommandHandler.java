package com.jehoon.food.order.service.domain;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jehoon.food.order.service.domain.dto.track.TrackOrderQuery;
import com.jehoon.food.order.service.domain.dto.track.TrackOrderResponse;
import com.jehoon.food.order.service.domain.entity.Order;
import com.jehoon.food.order.service.domain.exception.OrderNotFoundException;
import com.jehoon.food.order.service.domain.mapper.OrderDataMapper;
import com.jehoon.food.order.service.domain.ports.output.repository.OrderRepository;
import com.jehoon.food.order.service.domain.valueobject.TrackingId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;
    private final OrderRepository orderRepository;

    private static final String ORDER_NOT_FOUND_MSG = "주문을 찾을 수 없습니다. 트래킹 ID: {}";

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        // 주문 조회
        Optional<Order> orderResult = orderRepository
                .findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));

        // 주문이 존재하지 않는 경우 예외 처리
        if (orderResult.isEmpty()) {
            String errorMessage = String.format(ORDER_NOT_FOUND_MSG, trackOrderQuery.getOrderTrackingId());
            log.warn(errorMessage);
            throw new OrderNotFoundException(errorMessage);
        }

        // 주문이 존재하면 응답 객체 반환
        return orderDataMapper.orderToTrackOrderResponse(orderResult.get());
    }
}

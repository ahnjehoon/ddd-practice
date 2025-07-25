package com.jehoon.food.order.service.application.rest;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jehoon.food.order.application.dto.create.CreateOrderCommand;
import com.jehoon.food.order.application.dto.create.CreateOrderResponse;
import com.jehoon.food.order.application.dto.track.TrackOrderQuery;
import com.jehoon.food.order.application.dto.track.TrackOrderResponse;
import com.jehoon.food.order.application.ports.input.service.OrderApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {
    private static final String CREATE_ORDER_LOG = "고객 ID: {}, 레스토랑 ID: {}로 주문을 생성합니다.";
    private static final String ORDER_CREATED_LOG = "추적 ID: {}로 주문이 생성되었습니다.";
    private static final String TRACK_ORDER_LOG = "추적 ID: {}로 주문 상태를 조회합니다.";
    private static final String RETURN_ORDER_STATUS_LOG = "추적 ID: {}로 주문 상태를 반환합니다.";

    private final OrderApplicationService orderApplicationService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderCommand createOrderCommand) {

        log.info(CREATE_ORDER_LOG,
                createOrderCommand.getCustomerId(),
                createOrderCommand.getRestaurantId());

        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);

        log.info(ORDER_CREATED_LOG, createOrderResponse.getOrderTrackingId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderResponse);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> trackOrder(@PathVariable(value = "trackingId") UUID trackingId) {

        log.info(TRACK_ORDER_LOG, trackingId);

        TrackOrderQuery trackOrderQuery = TrackOrderQuery.builder()
                .orderTrackingId(trackingId)
                .build();

        TrackOrderResponse trackOrderResponse = orderApplicationService.trackOrder(trackOrderQuery);

        log.info(RETURN_ORDER_STATUS_LOG, trackOrderResponse.getOrderTrackingId());

        return ResponseEntity.ok(trackOrderResponse);
    }
}

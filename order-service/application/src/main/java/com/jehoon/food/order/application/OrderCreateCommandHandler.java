package com.jehoon.food.order.application;

import org.springframework.stereotype.Component;

import com.jehoon.food.order.application.dto.create.CreateOrderCommand;
import com.jehoon.food.order.application.dto.create.CreateOrderResponse;
import com.jehoon.food.order.application.mapper.OrderDataMapper;
import com.jehoon.food.order.application.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.jehoon.food.order.domain.event.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    public static final String ORDER_CREATED_LOG = "주문이 생성되었습니다. 주문 아이디: {}";
    public static final String ORDER_CREATED_SUCCESS_MSG = "주문이 성공적으로 생성되었습니다.";

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        // 주문 생성 이벤트 처리
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);

        // 로그 출력
        log.info(ORDER_CREATED_LOG, orderCreatedEvent.getOrder().getId().getValue());

        // 결제 요청 메시지 발행
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);

        // 주문 응답 생성
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(), ORDER_CREATED_SUCCESS_MSG);
    }
}

package com.jehoon.food.order.service.domain;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jehoon.food.order.service.domain.dto.create.CreateOrderCommand;
import com.jehoon.food.order.service.domain.entity.Order;
import com.jehoon.food.order.service.domain.entity.Restaurant;
import com.jehoon.food.order.service.domain.event.OrderCreatedEvent;
import com.jehoon.food.order.service.domain.exception.OrderDomainException;
import com.jehoon.food.order.service.domain.mapper.OrderDataMapper;
import com.jehoon.food.order.service.domain.ports.output.repository.CustomerRepository;
import com.jehoon.food.order.service.domain.ports.output.repository.OrderRepository;
import com.jehoon.food.order.service.domain.ports.output.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    private static final String RESTAURANT_NOT_FOUND_MSG = "레스토랑 ID로 레스토랑을 찾을 수 없습니다: ";
    private static final String CUSTOMER_NOT_FOUND_MSG = "고객 ID로 고객을 찾을 수 없습니다: ";
    private static final String ORDER_SAVE_FAILED_MSG = "주문을 저장할 수 없습니다!";
    private static final String ORDER_CREATED_MSG = "주문이 생성되었습니다. 주문 ID: {}";
    private static final String ORDER_SAVED_MSG = "주문이 저장되었습니다. 주문 ID: {}";

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        saveOrder(order);
        log.info(ORDER_CREATED_MSG, orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        return restaurantRepository
                .findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand))
                .orElseThrow(() -> {
                    String message = RESTAURANT_NOT_FOUND_MSG + createOrderCommand.getRestaurantId();
                    log.warn(message);
                    return new OrderDomainException(message);
                });
    }

    private void checkCustomer(UUID customerId) {
        customerRepository.findCustomer(customerId)
                .orElseThrow(() -> {
                    String message = CUSTOMER_NOT_FOUND_MSG + customerId;
                    log.warn(message);
                    return new OrderDomainException(message);
                });
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if (orderResult == null) {
            log.error(ORDER_SAVE_FAILED_MSG);
            throw new OrderDomainException(ORDER_SAVE_FAILED_MSG);
        }
        log.info(ORDER_SAVED_MSG, orderResult.getId().getValue());
        return orderResult;
    }
}

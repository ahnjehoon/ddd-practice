package com.jehoon.food.order.service.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.jehoon.food.order.service.domain.entity.Order;
import com.jehoon.food.order.service.domain.entity.Product;
import com.jehoon.food.order.service.domain.entity.Restaurant;
import com.jehoon.food.order.service.domain.event.OrderCancelledEvent;
import com.jehoon.food.order.service.domain.event.OrderCreatedEvent;
import com.jehoon.food.order.service.domain.event.OrderPaidEvent;
import com.jehoon.food.order.service.domain.exception.OrderDomainException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final String UTC = "UTC";

    public static final String ORDER_INITIATED_LOG = "주문 아이디: {} 의 주문이 시작되었습니다.";
    public static final String ORDER_PAID_LOG = "주문 아이디: {} 의 주문이 결제되었습니다.";
    public static final String ORDER_APPROVED_LOG = "주문 아이디: {} 의 주문이 승인되었습니다.";
    public static final String ORDER_PAYMENT_CANCEL_LOG = "주문 아이디: {} 의 결제 취소가 진행 중입니다.";
    public static final String ORDER_CANCELLED_LOG = "주문 아이디: {} 의 주문이 취소되었습니다.";
    public static final String RESTAURANT_INACTIVE_LOG = "음식점 ID {}는 현재 활성화되지 않았습니다.";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info(ORDER_INITIATED_LOG, order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info(ORDER_PAID_LOG, order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info(ORDER_APPROVED_LOG, order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info(ORDER_PAYMENT_CANCEL_LOG, order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info(ORDER_CANCELLED_LOG, order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException(String.format(RESTAURANT_INACTIVE_LOG, restaurant.getId().getValue()));
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getOrderItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
            Product currentProduct = orderItem.getProduct();
            if (currentProduct.equals(restaurantProduct)) {
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(),
                        restaurantProduct.getPrice());
            }
        }));
    }
}

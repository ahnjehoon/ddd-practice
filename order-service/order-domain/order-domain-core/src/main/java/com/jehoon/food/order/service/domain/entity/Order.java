package com.jehoon.food.order.service.domain.entity;

import java.util.List;
import java.util.UUID;

import com.jehoon.food.domain.entity.AggregateRoot;
import com.jehoon.food.domain.valueobject.CustomerId;
import com.jehoon.food.domain.valueobject.Money;
import com.jehoon.food.domain.valueobject.OrderId;
import com.jehoon.food.domain.valueobject.OrderStatus;
import com.jehoon.food.domain.valueobject.RestaurantId;
import com.jehoon.food.order.service.domain.exception.OrderDomainException;
import com.jehoon.food.order.service.domain.valueobject.OrderItemId;
import com.jehoon.food.order.service.domain.valueobject.StreetAddress;
import com.jehoon.food.order.service.domain.valueobject.TrackingId;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Order extends AggregateRoot<OrderId> {
    public static final String ERROR_STATE_PAY = "결제를 진행할 수 없는 주문 상태입니다.";
    public static final String ERROR_STATE_APPROVE = "승인할 수 없는 주문 상태입니다.";
    public static final String ERROR_STATE_INIT_CANCEL = "취소 요청을 진행할 수 없는 주문 상태입니다.";
    public static final String ERROR_STATE_CANCEL = "주문을 취소할 수 없는 상태입니다.";
    public static final String ERROR_STATE_INITIALIZE = "주문이 이미 초기화되었거나 잘못된 상태입니다.";
    public static final String ERROR_TOTAL_PRICE_INVALID = "총 금액은 0보다 커야 합니다.";
    public static final String ERROR_TOTAL_PRICE_MISMATCH = "총 금액(%.2f)이 주문 항목 합계(%.2f)와 일치하지 않습니다.";
    public static final String ERROR_ITEM_PRICE_INVALID = "주문 항목의 가격(%.2f)이 상품(%s)에 대해 유효하지 않습니다.";

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress streetAddress;
    private final Money price;
    private final List<OrderItem> orderItems;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException(ERROR_STATE_PAY);
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException(ERROR_STATE_APPROVE);
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException(ERROR_STATE_INIT_CANCEL);
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages) {
        if (orderStatus != OrderStatus.CANCELLING && orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException(ERROR_STATE_CANCEL);
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        } else if (failureMessages != null) {
            this.failureMessages.addAll(
                    failureMessages.stream()
                            .filter(message -> !message.isEmpty())
                            .toList());
        }
    }

    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException(ERROR_STATE_INITIALIZE);
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException(ERROR_TOTAL_PRICE_INVALID);
        }
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = orderItems.stream()
                .map(orderItem -> {
                    validateItemPrice(orderItem);
                    return orderItem.getSubTotal();
                })
                .reduce(Money.ZERO, Money::add);

        if (!price.equals(orderItemsTotal)) {
            throw new OrderDomainException(String.format(
                    ERROR_TOTAL_PRICE_MISMATCH,
                    price.getAmount(), orderItemsTotal.getAmount()));
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException(String.format(
                    ERROR_ITEM_PRICE_INVALID,
                    orderItem.getPrice().getAmount(),
                    orderItem.getProduct().getId().getValue()));
        }
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem : orderItems) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }
}

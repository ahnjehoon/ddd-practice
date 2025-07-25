package com.jehoon.food.order.domain.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jehoon.food.domain.valueobject.CustomerId;
import com.jehoon.food.domain.valueobject.Money;
import com.jehoon.food.domain.valueobject.OrderStatus;
import com.jehoon.food.domain.valueobject.ProductId;
import com.jehoon.food.domain.valueobject.RestaurantId;
import com.jehoon.food.order.domain.exception.OrderDomainException;
import com.jehoon.food.order.domain.valueobject.StreetAddress;

class OrderTest {

    private Order order;
    private CustomerId customerId;
    private RestaurantId restaurantId;
    private StreetAddress orderAddress;
    private Money price;
    private List<OrderItem> orderItems;

    @BeforeEach
    void setUp() {
        customerId = new CustomerId(UUID.randomUUID());
        restaurantId = new RestaurantId(UUID.randomUUID());
        orderAddress = new StreetAddress(UUID.randomUUID(), "123 Main St", "12345", "Seoul");

        // 주문 아이템 생성 - BigDecimal scale을 맞춰서 생성
        Product product = new Product(new ProductId(UUID.randomUUID()), "Pizza", new Money(new BigDecimal("15000.00")));
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(2)
                .price(new Money(new BigDecimal("15000.00")))
                .subTotal(new Money(new BigDecimal("30000.00")))
                .build();

        orderItems = List.of(orderItem);
        price = new Money(new BigDecimal("30000.00"));

        order = Order.builder()
                .customerId(customerId)
                .restaurantId(restaurantId)
                .orderAddress(orderAddress)
                .price(price)
                .orderItems(orderItems)
                .build();
    }

    @Test
    @DisplayName("주문 초기화 테스트")
    void testInitializeOrder() {
        // When
        order.initializeOrder();

        // Then
        assertNotNull(order.getId());
        assertNotNull(order.getTrackingId());
        assertEquals(OrderStatus.PENDING, order.getOrderStatus());

        // 주문 아이템 ID 확인
        OrderItem firstItem = order.getOrderItems().get(0);
        assertNotNull(firstItem.getId());
        assertEquals(1L, firstItem.getId().getValue());
    }

    @Test
    @DisplayName("주문 검증 성공 테스트")
    void testValidateOrderSuccess() {
        // Given - 주문을 초기화하지 않은 상태에서 검증
        Order freshOrder = Order.builder()
                .customerId(customerId)
                .restaurantId(restaurantId)
                .orderAddress(orderAddress)
                .price(price)
                .orderItems(orderItems)
                .build();

        // When & Then
        assertDoesNotThrow(() -> freshOrder.validateOrder());
    }

    @Test
    @DisplayName("총 금액이 0인 경우 검증 실패")
    void testValidateOrderWithZeroPrice() {
        // Given
        Order invalidOrder = Order.builder()
                .customerId(customerId)
                .restaurantId(restaurantId)
                .orderAddress(orderAddress)
                .price(Money.ZERO)
                .orderItems(orderItems)
                .build();

        // When & Then
        OrderDomainException exception = assertThrows(OrderDomainException.class,
                invalidOrder::validateOrder);
        assertEquals(Order.ERROR_TOTAL_PRICE_INVALID, exception.getMessage());
    }

    @Test
    @DisplayName("총 금액과 아이템 합계가 다른 경우 검증 실패")
    void testValidateOrderWithPriceMismatch() {
        // Given
        Order invalidOrder = Order.builder()
                .customerId(customerId)
                .restaurantId(restaurantId)
                .orderAddress(orderAddress)
                .price(new Money(new BigDecimal("25000"))) // 잘못된 총액
                .orderItems(orderItems)
                .build();

        // When & Then
        OrderDomainException exception = assertThrows(OrderDomainException.class,
                invalidOrder::validateOrder);
        assertTrue(exception.getMessage().contains("25000.00"));
        assertTrue(exception.getMessage().contains("30000.00"));
    }

    @Test
    @DisplayName("주문 결제 성공 테스트")
    void testPayOrder() {
        // Given
        order.initializeOrder(); // PENDING 상태로 초기화

        // When
        order.pay();

        // Then
        assertEquals(OrderStatus.PAID, order.getOrderStatus());
    }

    @Test
    @DisplayName("PENDING 상태가 아닌 주문 결제 실패")
    void testPayOrderInvalidState() {
        // Given
        order.initializeOrder();
        order.pay(); // PAID 상태로 변경

        // When & Then
        OrderDomainException exception = assertThrows(OrderDomainException.class,
                order::pay);
        assertEquals(Order.ERROR_STATE_PAY, exception.getMessage());
    }

    @Test
    @DisplayName("주문 승인 성공 테스트")
    void testApproveOrder() {
        // Given
        order.initializeOrder(); // PENDING
        order.pay(); // PAID

        // When
        order.approve();

        // Then
        assertEquals(OrderStatus.APPROVED, order.getOrderStatus());
    }

    @Test
    @DisplayName("PAID 상태가 아닌 주문 승인 실패")
    void testApproveOrderInvalidState() {
        // Given
        order.initializeOrder(); // PENDING 상태

        // When & Then
        OrderDomainException exception = assertThrows(OrderDomainException.class,
                order::approve);
        assertEquals(Order.ERROR_STATE_APPROVE, exception.getMessage());
    }

    @Test
    @DisplayName("주문 취소 초기화 성공 테스트")
    void testInitCancelOrder() {
        // Given
        order.initializeOrder(); // PENDING
        order.pay(); // PAID
        List<String> failureMessages = List.of("결제 실패", "재고 부족");

        // When
        order.initCancel(failureMessages);

        // Then
        assertEquals(OrderStatus.CANCELLING, order.getOrderStatus());
        assertEquals(2, order.getFailureMessages().size());
        assertTrue(order.getFailureMessages().contains("결제 실패"));
        assertTrue(order.getFailureMessages().contains("재고 부족"));
    }

    @Test
    @DisplayName("주문 취소 성공 테스트")
    void testCancelOrder() {
        // Given
        order.initializeOrder(); // PENDING
        List<String> failureMessages = List.of("고객 요청");

        // When
        order.cancel(failureMessages);

        // Then
        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
        assertTrue(order.getFailureMessages().contains("고객 요청"));
    }

    @Test
    @DisplayName("잘못된 상태에서 주문 취소 실패")
    void testCancelOrderInvalidState() {
        // Given
        order.initializeOrder(); // PENDING
        order.pay(); // PAID
        order.approve(); // APPROVED (취소 불가능한 상태)
        List<String> failureMessages = List.of("고객 요청");

        // When & Then
        OrderDomainException exception = assertThrows(OrderDomainException.class,
                () -> order.cancel(failureMessages));
        assertEquals(Order.ERROR_STATE_CANCEL, exception.getMessage());
    }
}
package com.jehoon.food.order.domain;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jehoon.food.domain.valueobject.CustomerId;
import com.jehoon.food.domain.valueobject.Money;
import com.jehoon.food.domain.valueobject.OrderStatus;
import com.jehoon.food.domain.valueobject.ProductId;
import com.jehoon.food.domain.valueobject.RestaurantId;
import com.jehoon.food.order.domain.entity.Order;
import com.jehoon.food.order.domain.entity.OrderItem;
import com.jehoon.food.order.domain.entity.Product;
import com.jehoon.food.order.domain.entity.Restaurant;
import com.jehoon.food.order.domain.event.OrderCancelledEvent;
import com.jehoon.food.order.domain.event.OrderCreatedEvent;
import com.jehoon.food.order.domain.event.OrderPaidEvent;
import com.jehoon.food.order.domain.exception.OrderDomainException;
import com.jehoon.food.order.domain.valueobject.StreetAddress;

@ExtendWith(MockitoExtension.class)
class OrderDomainServiceTest {

    private OrderDomainService orderDomainService;
    private Order order;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        orderDomainService = new OrderDomainServiceImpl();

        // 테스트용 Product 생성 - BigDecimal scale을 맞춰서 생성
        Product product = new Product(new ProductId(UUID.randomUUID()), "Pizza", new Money(new BigDecimal("15000.00")));

        // 테스트용 Restaurant 생성
        restaurant = Restaurant.builder()
                .restaurantId(new RestaurantId(UUID.randomUUID()))
                .products(List.of(product))
                .active(true)
                .build();

        // 테스트용 OrderItem 생성
        OrderItem orderItem = OrderItem.builder()
                .product(new Product(product.getId())) // ID만 있는 Product
                .quantity(2)
                .price(new Money(new BigDecimal("15000.00")))
                .subTotal(new Money(new BigDecimal("30000.00")))
                .build();

        // 테스트용 Order 생성
        order = Order.builder()
                .customerId(new CustomerId(UUID.randomUUID()))
                .restaurantId(restaurant.getId())
                .orderAddress(new StreetAddress(UUID.randomUUID(), "123 Main St", "12345", "Seoul"))
                .price(new Money(new BigDecimal("30000.00")))
                .orderItems(List.of(orderItem))
                .build();
    }

    @Test
    @DisplayName("주문 검증 및 초기화 성공 테스트")
    void testValidateAndInitiateOrderSuccess() {
        // When
        OrderCreatedEvent event = orderDomainService.validateAndInitiateOrder(order, restaurant);

        // Then
        assertNotNull(event);
        assertNotNull(event.getOrder().getId());
        assertNotNull(event.getOrder().getTrackingId());
        assertEquals(OrderStatus.PENDING, event.getOrder().getOrderStatus());
        assertNotNull(event.getCreatedAt());

        // 주문 아이템의 상품 정보가 레스토랑 정보로 업데이트되었는지 확인
        OrderItem updatedItem = event.getOrder().getOrderItems().get(0);
        assertEquals("Pizza", updatedItem.getProduct().getName());
        assertEquals(new Money(new BigDecimal("15000.00")), updatedItem.getProduct().getPrice());
    }

    @Test
    @DisplayName("비활성 레스토랑으로 주문 생성 실패 테스트")
    void testValidateAndInitiateOrderWithInactiveRestaurant() {
        // Given
        Restaurant inactiveRestaurant = Restaurant.builder()
                .restaurantId(restaurant.getId())
                .products(restaurant.getProducts())
                .active(false) // 비활성 상태
                .build();

        // When & Then
        OrderDomainException exception = assertThrows(OrderDomainException.class,
                () -> orderDomainService.validateAndInitiateOrder(order, inactiveRestaurant));

        assertTrue(exception.getMessage().contains("활성화되지 않았습니다"));
    }

    @Test
    @DisplayName("주문 결제 성공 테스트")
    void testPayOrderSuccess() {
        // Given
        order.initializeOrder(); // PENDING 상태로 초기화

        // When
        OrderPaidEvent event = orderDomainService.payOrder(order);

        // Then
        assertNotNull(event);
        assertEquals(OrderStatus.PAID, event.getOrder().getOrderStatus());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    @DisplayName("잘못된 상태에서 주문 결제 실패 테스트")
    void testPayOrderInvalidState() {
        // Given
        order.initializeOrder();
        order.pay(); // 이미 PAID 상태

        // When & Then
        assertThrows(OrderDomainException.class,
                () -> orderDomainService.payOrder(order));
    }

    @Test
    @DisplayName("주문 승인 성공 테스트")
    void testApproveOrderSuccess() {
        // Given
        order.initializeOrder(); // PENDING
        order.pay(); // PAID

        // When
        orderDomainService.approveOrder(order);

        // Then
        assertEquals(OrderStatus.APPROVED, order.getOrderStatus());
    }

    @Test
    @DisplayName("결제 취소 초기화 성공 테스트")
    void testCancelOrderPaymentSuccess() {
        // Given
        order.initializeOrder(); // PENDING
        order.pay(); // PAID
        List<String> failureMessages = List.of("결제 처리 실패", "카드 한도 초과");

        // When
        OrderCancelledEvent event = orderDomainService.cancelOrderPayment(order, failureMessages);

        // Then
        assertNotNull(event);
        assertEquals(OrderStatus.CANCELLING, event.getOrder().getOrderStatus());
        assertEquals(2, event.getOrder().getFailureMessages().size());
        assertTrue(event.getOrder().getFailureMessages().contains("결제 처리 실패"));
        assertTrue(event.getOrder().getFailureMessages().contains("카드 한도 초과"));
        assertNotNull(event.getCreatedAt());
    }

    @Test
    @DisplayName("주문 취소 성공 테스트")
    void testCancelOrderSuccess() {
        // Given
        order.initializeOrder(); // PENDING
        List<String> failureMessages = List.of("고객 요청으로 인한 취소");

        // When
        orderDomainService.cancelOrder(order, failureMessages);

        // Then
        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
        assertTrue(order.getFailureMessages().contains("고객 요청으로 인한 취소"));
    }

    @Test
    @DisplayName("레스토랑에 없는 상품으로 주문 시 검증 실패")
    void testProductNotFoundInRestaurant() {
        // Given
        Product unknownProduct = new Product(new ProductId(UUID.randomUUID())); // 레스토랑에 없는 상품
        OrderItem orderItemWithUnknownProduct = OrderItem.builder()
                .product(unknownProduct)
                .quantity(1)
                .price(new Money(new BigDecimal("10000.00")))
                .subTotal(new Money(new BigDecimal("10000.00")))
                .build();

        Order orderWithUnknownProduct = Order.builder()
                .customerId(new CustomerId(UUID.randomUUID()))
                .restaurantId(restaurant.getId())
                .orderAddress(new StreetAddress(UUID.randomUUID(), "123 Main St", "12345", "Seoul"))
                .price(new Money(new BigDecimal("10000.00")))
                .orderItems(List.of(orderItemWithUnknownProduct))
                .build();

        // When & Then - 레스토랑에 없는 상품으로 주문 시 검증 실패
        OrderDomainException exception = assertThrows(OrderDomainException.class,
                () -> orderDomainService.validateAndInitiateOrder(orderWithUnknownProduct, restaurant));

        assertTrue(exception.getMessage().contains("주문 항목의 가격"));
        assertTrue(exception.getMessage().contains("유효하지 않습니다"));
    }
}
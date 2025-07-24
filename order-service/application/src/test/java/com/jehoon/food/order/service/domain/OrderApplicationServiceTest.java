package com.jehoon.food.order.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jehoon.food.domain.valueobject.CustomerId;
import com.jehoon.food.domain.valueobject.Money;
import com.jehoon.food.domain.valueobject.OrderId;
import com.jehoon.food.domain.valueobject.OrderStatus;
import com.jehoon.food.domain.valueobject.ProductId;
import com.jehoon.food.domain.valueobject.RestaurantId;
import com.jehoon.food.order.service.domain.dto.create.CreateOrderCommand;
import com.jehoon.food.order.service.domain.dto.create.CreateOrderResponse;
import com.jehoon.food.order.service.domain.dto.create.OrderAddressDto;
import com.jehoon.food.order.service.domain.dto.create.OrderItemDto;
import com.jehoon.food.order.service.domain.entity.Customer;
import com.jehoon.food.order.service.domain.entity.Order;
import com.jehoon.food.order.service.domain.entity.Product;
import com.jehoon.food.order.service.domain.entity.Restaurant;
import com.jehoon.food.order.service.domain.exception.OrderDomainException;
import com.jehoon.food.order.service.domain.mapper.OrderDataMapper;
import com.jehoon.food.order.service.domain.ports.input.service.OrderApplicationService;
import com.jehoon.food.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.jehoon.food.order.service.domain.ports.output.repository.CustomerRepository;
import com.jehoon.food.order.service.domain.ports.output.repository.OrderRepository;
import com.jehoon.food.order.service.domain.ports.output.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderApplicationService 테스트")
class OrderApplicationServiceTest {

    private OrderApplicationService orderApplicationService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    private TestDataBuilder testDataBuilder;

    @BeforeEach
    void setUp() {
        testDataBuilder = new TestDataBuilder();

        OrderDataMapper orderDataMapper = new OrderDataMapper();
        OrderDomainService orderDomainService = new OrderDomainServiceImpl();
        OrderCreateHelper orderCreateHelper = new OrderCreateHelper(orderDomainService, orderRepository, customerRepository, restaurantRepository, orderDataMapper);
        OrderCreateCommandHandler orderCreateCommandHandler = new OrderCreateCommandHandler(orderCreateHelper, orderDataMapper, orderCreatedPaymentRequestMessagePublisher);
        orderApplicationService = new OrderApplicationServiceImpl(orderCreateCommandHandler, null); // trackOrderCommandHandler is not tested

        mockCustomerFound();
        mockOrderRepositorySave();
    }

    private void mockCustomerFound() {
        given(customerRepository.findCustomer(TestDataBuilder.CUSTOMER_ID))
                .willReturn(Optional.of(testDataBuilder.createCustomer()));
    }

    private void mockOrderRepositorySave() {
        given(orderRepository.save(any(Order.class)))
                .willAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    order.setId(new OrderId(TestDataBuilder.ORDER_ID));
                    return order;
                });
    }

    private void mockRestaurantInfo(Restaurant restaurant) {
        given(restaurantRepository.findRestaurantInformation(any(Restaurant.class)))
                .willReturn(Optional.of(restaurant));
    }

    @Nested
    @DisplayName("주문 생성")
    class CreateOrder {

        @Test
        @DisplayName("성공적으로 주문을 생성한다")
        void shouldCreateOrderSuccessfully() {
            CreateOrderCommand command = testDataBuilder.createValidOrderCommand();
            mockRestaurantInfo(testDataBuilder.createActiveRestaurant());

            CreateOrderResponse response = orderApplicationService.createOrder(command);

            assertThat(response).satisfies(r -> {
                assertThat(r.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
                assertThat(r.getMessage()).isEqualTo(OrderCreateCommandHandler.ORDER_CREATED_SUCCESS_MSG);
                assertThat(r.getOrderTrackingId()).isNotNull();
            });

            then(orderRepository).should().save(any(Order.class));
        }
    }

    @Nested
    @DisplayName("주문 생성 실패")
    class CreateOrderFailure {

        @Test
        @DisplayName("총 금액 불일치로 예외 발생")
        void shouldThrowOnIncorrectTotalPrice() {
            CreateOrderCommand command = testDataBuilder.createOrderCommandWithWrongTotalPrice();
            mockRestaurantInfo(testDataBuilder.createActiveRestaurant());

            String resultMsg = String.format(Order.ERROR_TOTAL_PRICE_MISMATCH,
                    TestDataBuilder.WRONG_TOTAL_PRICE, TestDataBuilder.CORRECT_TOTAL_PRICE);

            assertThatThrownBy(() -> orderApplicationService.createOrder(command))
                    .isInstanceOf(OrderDomainException.class)
                    .hasMessage(resultMsg);
        }

        @Test
        @DisplayName("상품 가격 오류로 예외 발생")
        void shouldThrowOnIncorrectProductPrice() {
            CreateOrderCommand command = testDataBuilder.createOrderCommandWithWrongProductPrice();
            mockRestaurantInfo(testDataBuilder.createActiveRestaurant());

            String resultMsg = String.format(Order.ERROR_ITEM_PRICE_INVALID,
                    TestDataBuilder.WRONG_PRODUCT_PRICE, TestDataBuilder.PRODUCT_ID);

            assertThatThrownBy(() -> orderApplicationService.createOrder(command))
                    .isInstanceOf(OrderDomainException.class)
                    .hasMessage(resultMsg);
        }

        @Test
        @DisplayName("비활성화된 레스토랑으로 주문 시 예외 발생")
        void shouldThrowWhenRestaurantIsInactive() {
            CreateOrderCommand command = testDataBuilder.createValidOrderCommand();
            mockRestaurantInfo(testDataBuilder.createInactiveRestaurant());

            String resultMsg = String.format(OrderDomainServiceImpl.RESTAURANT_INACTIVE_LOG,
                    TestDataBuilder.RESTAURANT_ID);

            assertThatThrownBy(() -> orderApplicationService.createOrder(command))
                    .isInstanceOf(OrderDomainException.class)
                    .hasMessage(resultMsg);
        }
    }

    @Nested
    @DisplayName("데이터 검증")
    class DataValidation {

        @Test
        @DisplayName("존재하지 않는 고객")
        void shouldThrowWhenCustomerNotFound() {
            given(customerRepository.findCustomer(TestDataBuilder.CUSTOMER_ID)).willReturn(Optional.empty());
            CreateOrderCommand command = testDataBuilder.createValidOrderCommand();

            assertThatThrownBy(() -> orderApplicationService.createOrder(command))
                    .isInstanceOf(OrderDomainException.class);
        }

        @Test
        @DisplayName("존재하지 않는 레스토랑")
        void shouldThrowWhenRestaurantNotFound() {
            CreateOrderCommand command = testDataBuilder.createValidOrderCommand();
            given(restaurantRepository.findRestaurantInformation(any(Restaurant.class)))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> orderApplicationService.createOrder(command))
                    .isInstanceOf(OrderDomainException.class);
        }
    }

    /**
     * 테스트 데이터 생성 도우미
     */
    private static class TestDataBuilder {
        static final UUID CUSTOMER_ID = UUID.fromString("896f4078-2ca9-4f1c-a014-3f6fc8532a7c");
        static final UUID RESTAURANT_ID = UUID.fromString("362ba33d-2587-44df-977a-34d1d194164f");
        static final UUID PRODUCT_ID = UUID.fromString("b463316b-1488-4f3b-a166-029583619e93");
        static final UUID ORDER_ID = UUID.fromString("112e2133-a4a6-4ec1-94b8-fe61e7d08701");

        static final BigDecimal PRODUCT_PRICE = new BigDecimal("50.00");
        static final BigDecimal CORRECT_TOTAL_PRICE = new BigDecimal("200.00");
        static final BigDecimal WRONG_TOTAL_PRICE = new BigDecimal("250.00");
        static final BigDecimal WRONG_PRODUCT_PRICE = new BigDecimal("60.00");

        private static final String STREET = "street_1";
        private static final String POSTAL_CODE = "1000AB";
        private static final String CITY = "Paris";

        Customer createCustomer() {
            return new Customer(new CustomerId(CUSTOMER_ID));
        }

        Restaurant createActiveRestaurant() {
            return createRestaurant(true);
        }

        Restaurant createInactiveRestaurant() {
            return createRestaurant(false);
        }

        private Restaurant createRestaurant(boolean active) {
            return Restaurant.builder()
                    .restaurantId(new RestaurantId(RESTAURANT_ID))
                    .products(createProducts())
                    .active(active)
                    .build();
        }

        private List<Product> createProducts() {
            return List.of(
                    new Product(new ProductId(PRODUCT_ID), "product-1", new Money(PRODUCT_PRICE)),
                    new Product(new ProductId(UUID.randomUUID()), "product-2", new Money(PRODUCT_PRICE)));
        }

        CreateOrderCommand createValidOrderCommand() {
            return CreateOrderCommand.builder()
                    .customerId(CUSTOMER_ID)
                    .restaurantId(RESTAURANT_ID)
                    .address(createAddress())
                    .price(CORRECT_TOTAL_PRICE)
                    .items(createValidOrderItems())
                    .build();
        }

        CreateOrderCommand createOrderCommandWithWrongTotalPrice() {
            return CreateOrderCommand.builder()
                    .customerId(CUSTOMER_ID)
                    .restaurantId(RESTAURANT_ID)
                    .address(createAddress())
                    .price(WRONG_TOTAL_PRICE)
                    .items(createValidOrderItems())
                    .build();
        }

        CreateOrderCommand createOrderCommandWithWrongProductPrice() {
            return CreateOrderCommand.builder()
                    .customerId(CUSTOMER_ID)
                    .restaurantId(RESTAURANT_ID)
                    .address(createAddress())
                    .price(new BigDecimal("210.00"))
                    .items(createOrderItemsWithWrongPrice())
                    .build();
        }

        private OrderAddressDto createAddress() {
            return OrderAddressDto.builder()
                    .street(STREET)
                    .postalCode(POSTAL_CODE)
                    .city(CITY)
                    .build();
        }

        private List<OrderItemDto> createValidOrderItems() {
            return List.of(
                    createOrderItem(1, PRODUCT_PRICE, new BigDecimal("50.00")),
                    createOrderItem(3, PRODUCT_PRICE, new BigDecimal("150.00")));
        }

        private List<OrderItemDto> createOrderItemsWithWrongPrice() {
            return List.of(
                    createOrderItem(1, WRONG_PRODUCT_PRICE, new BigDecimal("60.00")),
                    createOrderItem(3, PRODUCT_PRICE, new BigDecimal("150.00")));
        }

        private OrderItemDto createOrderItem(int quantity, BigDecimal price, BigDecimal subTotal) {
            return OrderItemDto.builder()
                    .productId(PRODUCT_ID)
                    .quantity(quantity)
                    .price(price)
                    .subTotal(subTotal)
                    .build();
        }
    }
}

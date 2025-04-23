package com.jehoon.shop.order.application;

import com.jehoon.shop.order.domain.*;
import com.jehoon.shop.order.domain.exception.NoCancellablePermission;
import com.jehoon.shop.order.domain.exception.NoOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelOrderService {
    private OrderRepository orderRepository;

    public CancelOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void cancel(OrderNo orderNo, Canceller canceller) {
        Order order = orderRepository.findById(orderNo)
                .orElseThrow(NoOrderException::new);
        order.cancel();
    }

}

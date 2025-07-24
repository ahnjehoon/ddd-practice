package com.jehoon.food.order.service.domain;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.jehoon.food.order.service.domain.dto.message.PaymentResponse;
import com.jehoon.food.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Service
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {

    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {

    }
}

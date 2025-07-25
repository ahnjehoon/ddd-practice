package com.jehoon.food.order.application.ports.input.message.listener.payment;

import com.jehoon.food.order.application.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}

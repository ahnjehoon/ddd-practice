package com.jehoon.shop.order.domain;

public interface CancelPolicy {
    boolean hasCancellationPermission(Order order, Canceller canceller);
}

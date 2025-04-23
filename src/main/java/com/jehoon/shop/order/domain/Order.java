package com.jehoon.shop.order.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
@Access(AccessType.FIELD)
public class Order {
    @EmbeddedId
    private OrderNo number;

    private OrderState state;
    private ShippingInfo shippingInfo;

    public void changeShippingInfo(ShippingInfo newShippingInfo) {
        verifyNotYetShipped();
        setShippingInfo(newShippingInfo);
    }

    private void verifyNotYetShipped() {
        if (!isNotYetShipped())
            throw new IllegalArgumentException("배송상태를 변경할 수 없습니다. 현재상태: " + state);
    }

    public boolean isNotYetShipped() {
        return state == OrderState.PAYMENT_WAITING || state == OrderState.PREPARING;
    }

    private void setShippingInfo(ShippingInfo shippingInfo) {
        if (shippingInfo == null) throw new IllegalArgumentException("no shipping info");
        this.shippingInfo = shippingInfo;
    }

    public void cancel() {
        verifyNotYetShipped();
        this.state = OrderState.CANCELED;
    }
}

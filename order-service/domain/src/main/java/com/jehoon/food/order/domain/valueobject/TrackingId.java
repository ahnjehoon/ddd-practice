package com.jehoon.food.order.domain.valueobject;

import java.util.UUID;

import com.jehoon.food.domain.valueobject.BaseId;

public class TrackingId extends BaseId<UUID> {

    public TrackingId(UUID value) {
        super(value);
    }

}

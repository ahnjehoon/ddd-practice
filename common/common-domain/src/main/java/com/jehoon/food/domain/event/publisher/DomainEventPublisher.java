package com.jehoon.food.domain.event.publisher;

import com.jehoon.food.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}

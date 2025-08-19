
package com.vraj.ledgerforge.common.events;

import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent {
    public final String eventId = UUID.randomUUID().toString();
    public final Instant occurredAt = Instant.now();
    public abstract String aggregateId();
    public abstract String type();
}

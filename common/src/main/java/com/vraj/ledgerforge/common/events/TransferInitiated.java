
package com.vraj.ledgerforge.common.events;

public class TransferInitiated extends DomainEvent {
    public final String fromId;
    public final String toId;
    public final long amountCents;
    public TransferInitiated(String fromId, String toId, long amountCents) {
        this.fromId = fromId; this.toId = toId; this.amountCents = amountCents;
    }
    public String aggregateId() { return fromId + "->" + toId; }
    public String type() { return "TransferInitiated"; }
}


package com.vraj.ledgerforge.common.events;

public class AccountDebited extends DomainEvent {
    public final String accountId;
    public final long amountCents;
    public AccountDebited(String accountId, long amountCents) {
        this.accountId = accountId; this.amountCents = amountCents;
    }
    public String aggregateId() { return accountId; }
    public String type() { return "AccountDebited"; }
}

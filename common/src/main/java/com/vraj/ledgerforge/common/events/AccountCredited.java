
package com.vraj.ledgerforge.common.events;

public class AccountCredited extends DomainEvent {
    public final String accountId;
    public final long amountCents;
    public AccountCredited(String accountId, long amountCents) {
        this.accountId = accountId; this.amountCents = amountCents;
    }
    public String aggregateId() { return accountId; }
    public String type() { return "AccountCredited"; }
}

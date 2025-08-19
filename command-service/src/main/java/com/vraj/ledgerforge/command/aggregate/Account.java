
package com.vraj.ledgerforge.command.aggregate;

import com.vraj.ledgerforge.common.events.*;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private final String id;
    private long balanceCents;
    private final List<DomainEvent> newEvents = new ArrayList<>();

    public Account(String id, long balanceCents) {
        this.id = id; this.balanceCents = balanceCents;
    }

    public void credit(long amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
        apply(new AccountCredited(id, amount));
    }

    public void debit(long amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
        if (balanceCents < amount) throw new IllegalStateException("insufficient funds");
        apply(new AccountDebited(id, amount));
    }

    public void apply(DomainEvent ev) {
        if (ev instanceof AccountCredited ac) balanceCents += ac.amountCents;
        else if (ev instanceof AccountDebited ad) balanceCents -= ad.amountCents;
        newEvents.add(ev);
    }

    public List<DomainEvent> getNewEvents() { return newEvents; }
    public long getBalanceCents() { return balanceCents; }
    public String getId() { return id; }

    public static Account rehydrate(String id, long starting, List<DomainEvent> history) {
        Account acc = new Account(id, starting);
        for (DomainEvent ev : history) acc.apply(ev);
        acc.getNewEvents().clear();
        return acc;
    }
}

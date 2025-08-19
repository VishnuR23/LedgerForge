
package com.vraj.ledgerforge.query.readmodel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AccountBalanceEntity {
    @Id public String accountId;
    public long balanceCents;
}

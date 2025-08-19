
package com.vraj.ledgerforge.command.outbox;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "outbox")
public class OutboxEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable=false) public String topic;
    @Column(nullable=false, length=200) public String key;
    @Column(nullable=false, length=4000) public String payload;
    @Column(nullable=false) public Instant createdAt = Instant.now();
    @Column(nullable=false) public boolean published = false;
}

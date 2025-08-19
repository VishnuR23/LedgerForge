
package com.vraj.ledgerforge.command.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vraj.ledgerforge.command.outbox.OutboxEntity;
import com.vraj.ledgerforge.command.outbox.OutboxRepo;
import com.vraj.ledgerforge.common.events.AccountCredited;
import com.vraj.ledgerforge.common.events.AccountDebited;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final OutboxRepo outbox;
    private final RedissonClient redisson;
    private final ObjectMapper mapper;

    public AccountController(OutboxRepo outbox, RedissonClient redisson, ObjectMapper mapper) {
        this.outbox = outbox; this.redisson = redisson; this.mapper = mapper;
    }

    public record TxnRequest(@NotBlank String accountId, @Min(1) long amountCents) {}

    @PostMapping("/credit")
    @Transactional
    public ResponseEntity<?> credit(@RequestBody TxnRequest req) throws Exception {
        return enqueueEvent(new AccountCredited(req.accountId(), req.amountCents()));
    }

    @PostMapping("/debit")
    @Transactional
    public ResponseEntity<?> debit(@RequestBody TxnRequest req) throws Exception {
        return enqueueEvent(new AccountDebited(req.accountId(), req.amountCents()));
    }

    private ResponseEntity<?> enqueueEvent(Object ev) throws Exception {
        // Distributed idempotency lock per account to avoid double-spend
        String aggId = (String) ev.getClass().getMethod("aggregateId").invoke(ev);
        RLock lock = redisson.getLock("acct:" + aggId);
        if (!lock.tryLock(5, 2, TimeUnit.SECONDS)) {
            return ResponseEntity.status(429).body(Map.of("error","try again"));
        }
        try {
            OutboxEntity m = new OutboxEntity();
            m.topic = "ledger.events";
            m.key = aggId;
            m.payload = mapper.writeValueAsString(ev);
            outbox.save(m);
            return ResponseEntity.accepted().body(Map.of("status","queued","aggregateId", aggId));
        } finally {
            lock.unlock();
        }
    }
}

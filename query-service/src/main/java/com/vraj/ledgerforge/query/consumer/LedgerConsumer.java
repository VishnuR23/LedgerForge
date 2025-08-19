
package com.vraj.ledgerforge.query.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vraj.ledgerforge.query.readmodel.AccountBalanceEntity;
import com.vraj.ledgerforge.query.readmodel.AccountBalanceRepo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LedgerConsumer {
    private final AccountBalanceRepo repo;
    private final ObjectMapper mapper;

    public LedgerConsumer(AccountBalanceRepo repo, ObjectMapper mapper) {
        this.repo = repo; this.mapper = mapper;
    }

    @KafkaListener(topics = "ledger.events", groupId = "ledger-query")
    @Transactional
    public void onEvent(ConsumerRecord<String, String> rec) throws Exception {
        JsonNode n = mapper.readTree(rec.value());
        String type = n.get("type").asText();
        switch (type) {
            case "AccountCredited" -> {
                var id = n.get("accountId").asText();
                var amt = n.get("amountCents").asLong();
                var e = repo.findById(id).orElseGet(() -> { var x = new AccountBalanceEntity(); x.accountId = id; x.balanceCents = 0; return x; });
                e.balanceCents += amt;
                repo.save(e);
            }
            case "AccountDebited" -> {
                var id = n.get("accountId").asText();
                var amt = n.get("amountCents").asLong();
                var e = repo.findById(id).orElseGet(() -> { var x = new AccountBalanceEntity(); x.accountId = id; x.balanceCents = 0; return x; });
                e.balanceCents -= amt;
                repo.save(e);
            }
            default -> {}
        }
    }
}

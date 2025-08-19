
package com.vraj.ledgerforge.command.outbox;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxPublisher {
    private final OutboxRepo repo;
    private final KafkaTemplate<String, String> kafka;

    public OutboxPublisher(OutboxRepo repo, KafkaTemplate<String, String> kafka) {
        this.repo = repo; this.kafka = kafka;
    }

    @Scheduled(fixedDelay = 500)
    @Transactional
    public void flushOutbox() {
        var batch = repo.findTop50ByPublishedFalseOrderByCreatedAtAsc();
        for (var msg : batch) {
            kafka.send(msg.topic, msg.key, msg.payload);
            msg.published = true;
            repo.save(msg);
        }
    }
}

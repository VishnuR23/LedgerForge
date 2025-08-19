
package com.vraj.ledgerforge.command.outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxRepo extends JpaRepository<OutboxEntity, Long> {
    List<OutboxEntity> findTop50ByPublishedFalseOrderByCreatedAtAsc();
}

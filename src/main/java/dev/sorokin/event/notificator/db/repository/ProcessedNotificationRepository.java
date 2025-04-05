package dev.sorokin.event.notificator.db.repository;

import dev.sorokin.event.notificator.db.entity.IdempotencyMessageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedNotificationRepository extends JpaRepository<IdempotencyMessageEntity, Long> {

    boolean existsByMessageId(String messageId);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM idempotency m
            WHERE (m.created_at + INTERVAL '1 WEEK') < CURRENT_TIMESTAMP
            """, nativeQuery = true)
    void deleteAllForLatestSevenDays();
}

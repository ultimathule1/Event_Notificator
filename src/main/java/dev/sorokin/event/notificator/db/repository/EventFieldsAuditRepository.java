package dev.sorokin.event.notificator.db.repository;

import dev.sorokin.event.notificator.db.entity.EventFieldsAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventFieldsAuditRepository extends JpaRepository<EventFieldsAuditEntity, Long> {

    @Query("""
            SELECT e FROM EventFieldsAuditEntity e
            WHERE e.eventFieldsId = :eventFieldsId
                AND e.typeOperation = :typeOperation
            """)
    Optional<EventFieldsAuditEntity> findLatestUpdatedAuditByEventFieldsId(Long eventFieldsId, String typeOperation);

    void deleteByEventFieldsId(Long eventFieldsId);

    Optional<EventFieldsAuditEntity> findByEventFieldsIdAndTypeOperationEquals(Long eventFieldsId, String typeOperation);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM event_fields_audit e
            WHERE e.type_operation = :operationType AND (e.changed_at + INTERVAL '1 MONTH') < CURRENT_TIMESTAMP
            RETURNING event_fields_id
            """, nativeQuery = true)
    List<Long> deleteAllByOperationExpiredForMoreThanMonth(String operationType);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM EventFieldsAuditEntity e
            WHERE e.eventFieldsId IN :ids
            """)
    void deleteAllByEventFieldsIds(@Param("ids") List<Long> ids);
}
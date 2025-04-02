package dev.sorokin.event.notificator.db.repository;

import dev.sorokin.event.notificator.db.entity.EventFieldsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventFieldsRepository extends JpaRepository<EventFieldsEntity, Long> {
}

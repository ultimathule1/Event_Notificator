package dev.sorokin.event.notificator.domain.service;

import dev.sorokin.event.notificator.db.OperationAuditType;
import dev.sorokin.event.notificator.db.entity.EventFieldsAuditEntity;
import dev.sorokin.event.notificator.db.entity.EventFieldsEntity;
import dev.sorokin.event.notificator.db.repository.EventFieldsAuditRepository;
import dev.sorokin.event.notificator.db.repository.EventFieldsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class EventFieldsService {
    @PersistenceContext
    private EntityManager entityManager;
    private final EventFieldsRepository eventFieldsRepository;
    private final EventFieldsAuditRepository eventFieldsAuditRepository;

    public EventFieldsService(EventFieldsRepository eventFieldsRepository, EventFieldsAuditRepository eventFieldsAuditRepository) {
        this.eventFieldsRepository = eventFieldsRepository;
        this.eventFieldsAuditRepository = eventFieldsAuditRepository;
    }

    @Transactional
    public EventFieldsEntity create(EventFieldsEntity eventFieldsEntity, Long changedBy) {
        EventFieldsEntity savedEntity = eventFieldsRepository.save(eventFieldsEntity);

        EventFieldsAuditEntity auditEntity = defaultCreatorFromEventFieldsToAuditEntity(
                savedEntity,
                changedBy,
                OperationAuditType.CREATE
        );

        eventFieldsAuditRepository.save(auditEntity);

        return savedEntity;
    }

    @Transactional
    public EventFieldsEntity update(Long id, EventFieldsEntity eventFieldsEntity, Long changedBy) {
        EventFieldsEntity foundFieldsEntity = findById(id);

        if (isRemovedByEventFieldsId(foundFieldsEntity.getId())) {
            throw new IllegalArgumentException("EventFieldsEntity with id " + id +
                    " is removed. You can't update this entity");
        }

        EventFieldsAuditEntity auditEntity = defaultCreatorFromEventFieldsToAuditEntity(
                foundFieldsEntity,
                changedBy,
                OperationAuditType.UPDATE
        );
        eventFieldsAuditRepository.save(auditEntity);

        foundFieldsEntity.setName(eventFieldsEntity.getName());
        foundFieldsEntity.setDate(eventFieldsEntity.getDate());
        foundFieldsEntity.setDuration(eventFieldsEntity.getDuration());
        foundFieldsEntity.setCost(eventFieldsEntity.getCost());
        foundFieldsEntity.setLocationId(eventFieldsEntity.getLocationId());
        foundFieldsEntity.setMaxPlaces(eventFieldsEntity.getMaxPlaces());
        foundFieldsEntity.setStatus(eventFieldsEntity.getStatus());

        return eventFieldsRepository.save(foundFieldsEntity);
    }

    @Transactional
    public EventFieldsEntity remove(Long id, Long changedBy) {
        EventFieldsEntity fountEntity = findById(id);
        if (isRemovedByEventFieldsId(fountEntity.getId())) {
            throw new IllegalArgumentException("EventFieldsEntity with id " + id + " is already removed");
        }

        EventFieldsAuditEntity auditEntity = defaultCreatorFromEventFieldsToAuditEntity(
                fountEntity,
                changedBy,
                OperationAuditType.REMOVE
        );

        eventFieldsAuditRepository.save(auditEntity);

        return fountEntity;
    }

    @Transactional
    public void removeAll(List<Long> eventFieldsIds, Long changedBy) {
        if (eventFieldsIds == null || eventFieldsIds.isEmpty()) {
            return;
        }

        List<EventFieldsEntity> eventFieldsEntities = eventFieldsRepository.findAllById(eventFieldsIds);

        List<EventFieldsAuditEntity> eventFieldsAuditEntitiesToRemove = new ArrayList<>();
        eventFieldsEntities.forEach(n -> {
            eventFieldsAuditEntitiesToRemove.add(defaultCreatorFromEventFieldsToAuditEntity(
                    n,
                    changedBy,
                    OperationAuditType.REMOVE
            ));
        });

        int batchSize = 20;
        for (int i = 0; i < eventFieldsAuditEntitiesToRemove.size(); i++) {
            entityManager.persist(eventFieldsAuditEntitiesToRemove.get(i));
            if ((i + 1) % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public EventFieldsEntity remove(Long id) {
        return remove(id, null);
    }

    @Transactional
    public void delete(Long id) {
        EventFieldsEntity fountEntity = findById(id);

        eventFieldsAuditRepository.deleteByEventFieldsId(fountEntity.getId());
        eventFieldsRepository.delete(fountEntity);
    }

    @Transactional
    public void deleteAllRemovableEntitiesWhoExpiredForMoreMonth() {
        var eventFieldsIdsForRemove =
                eventFieldsAuditRepository.deleteAllByOperationExpiredForMoreThanMonth(OperationAuditType.REMOVE.getName());

        HashSet<Long> uniqueIdsForDeletingEventFields = new HashSet<>(eventFieldsIdsForRemove);

        eventFieldsAuditRepository.deleteAllByEventFieldsIds(uniqueIdsForDeletingEventFields.stream().toList());
        eventFieldsRepository.deleteAllByIdInBatch(uniqueIdsForDeletingEventFields);
    }

    public EventFieldsEntity findById(Long id) {
        return eventFieldsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + id));
    }

    public EventFieldsEntity findLatestUpdatedEventFields(Long id) {
        EventFieldsAuditEntity auditEntity = eventFieldsAuditRepository
                .findFirstByEventFieldsIdAndTypeOperationOrderByDateDesc(id, OperationAuditType.UPDATE.getName())
                .orElseThrow(() -> new EntityNotFoundException("Could not find audit entity with id: " + id));

        return new EventFieldsEntity(auditEntity);
    }

    private EventFieldsAuditEntity defaultCreatorFromEventFieldsToAuditEntity(
            EventFieldsEntity fieldsEntity,
            Long changedBy,
            OperationAuditType operationAuditType) {

        return EventFieldsAuditEntity.builder()
                .eventFieldsId(fieldsEntity.getId())
                .date(fieldsEntity.getDate())
                .cost(fieldsEntity.getCost())
                .locationId(fieldsEntity.getLocationId())
                .duration(fieldsEntity.getDuration())
                .maxPlaces(fieldsEntity.getMaxPlaces())
                .status(fieldsEntity.getStatus())
                .name(fieldsEntity.getName())

                .changedAt(LocalDateTime.now())
                .typeOperation(operationAuditType.getName())
                .changedBy(changedBy)

                .build();
    }

    private boolean isRemovedByEventFieldsId(Long eventFieldsId) {
        Optional<EventFieldsAuditEntity> auditEntity = eventFieldsAuditRepository
                .findByEventFieldsIdAndTypeOperationEquals(eventFieldsId, OperationAuditType.REMOVE.getName());

        return auditEntity.isPresent();
    }
}
package dev.sorokin.event.notificator.domain.service;

import dev.sorokin.event.notificator.db.entity.EventFieldsEntity;
import dev.sorokin.event.notificator.db.entity.NotificationChangedEventEntity;
import dev.sorokin.event.notificator.db.repository.NotificationsRepository;
import dev.sorokin.event.notificator.domain.ChangedEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class KafkaEventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventService.class);
    private final NotificationsRepository notificationsRepository;
    private final EventFieldsService eventFieldsService;

    public KafkaEventService(NotificationsRepository notificationsRepository, EventFieldsService eventFieldsService) {
        this.notificationsRepository = notificationsRepository;
        this.eventFieldsService = eventFieldsService;
    }

    @Transactional
    public void addAll(ChangedEvent changedEvent, List<Long> subscribers) {
        LOGGER.info("Try adding changed event {} to subscribers", changedEvent);

        if (changedEvent == null) {
            LOGGER.error("Changed event is null in addAll() method");
            return;
        }

        if (subscribers.isEmpty()) {
            LOGGER.info("The notification will not be save because does not have any subscribers");
            return;
        }

        EventFieldsEntity eventOldFieldsEntity = initializeOldFieldsEntity(changedEvent);
        EventFieldsEntity fieldsOldEntity =
                eventFieldsService.create(eventOldFieldsEntity, changedEvent.changedEventByUserId());

        EventFieldsEntity eventNewFieldsEntity = initializeNewFieldsEntity(changedEvent);
        EventFieldsEntity fieldsNewEntity =
                eventFieldsService.update(fieldsOldEntity.getId(), eventNewFieldsEntity, changedEvent.changedEventByUserId());

        List<NotificationChangedEventEntity> changedEventEntities = new ArrayList<>();
        subscribers
                .forEach((sId) -> {
                    NotificationChangedEventEntity notificationChangedEventEntity = new NotificationChangedEventEntity();
                    initializeChangedEventEntity(notificationChangedEventEntity, changedEvent, sId);

                    notificationChangedEventEntity.setFieldsEntity(fieldsNewEntity);
                    changedEventEntities.add(notificationChangedEventEntity);
                });
        notificationsRepository.saveAll(changedEventEntities);
        LOGGER.info("SUCCESS! Changed event added to subscribers = {}", changedEvent);
    }

    private void initializeChangedEventEntity(NotificationChangedEventEntity notificationChangedEventEntity, ChangedEvent changedEvent, Long subId) {
        notificationChangedEventEntity.setEventId(changedEvent.eventId());
        notificationChangedEventEntity.setOwnerEventId(changedEvent.ownerEventId());
        notificationChangedEventEntity.setIsRead(false);
        notificationChangedEventEntity.setSubscriberId(subId);
        notificationChangedEventEntity.setUserChangedId(changedEvent.changedEventByUserId());
        notificationChangedEventEntity.setCreatedAt(LocalDateTime.now());
    }

    private EventFieldsEntity initializeOldFieldsEntity(ChangedEvent changedEvent) {
        return EventFieldsEntity.builder()
                .cost(changedEvent.costOld())
                .date(changedEvent.dateOld())
                .name(changedEvent.nameOld())
                .duration(changedEvent.durationOld())
                .locationId(changedEvent.locationIdOld())
                .maxPlaces(changedEvent.maxPlacesOld())
                .status(changedEvent.statusOld())
                .build();
    }

    private EventFieldsEntity initializeNewFieldsEntity(ChangedEvent changedEvent) {
        return EventFieldsEntity.builder()
                .cost(changedEvent.costNew())
                .date(changedEvent.dateNew())
                .name(changedEvent.nameNew())
                .duration(changedEvent.durationNew())
                .locationId(changedEvent.locationIdNew())
                .maxPlaces(changedEvent.maxPlacesNew())
                .status(changedEvent.statusNew())
                .build();
    }
}
package dev.sorokin.event.notificator.handler;

import dev.sorokin.event.notificator.api.dto.event.EventChangerEvent;
import dev.sorokin.event.notificator.db.entity.IdempotencyMessageEntity;
import dev.sorokin.event.notificator.db.repository.ProcessedNotificationRepository;
import dev.sorokin.event.notificator.domain.service.KafkaEventService;
import dev.sorokin.event.notificator.exception.kafka.NotRetryableException;
import dev.sorokin.event.notificator.mapper.EventKafkaMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@KafkaListener(topics = {
        "${events.notifications.topic.name}"
})
public class KafkaEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventHandler.class);
    private final KafkaEventService kafkaEventService;
    private final ProcessedNotificationRepository processedNotificationRepository;
    private final EventKafkaMapper kafkaMapper;

    public KafkaEventHandler(KafkaEventService kafkaEventService, ProcessedNotificationRepository processedNotificationRepository, EventKafkaMapper kafkaMapper) {
        this.kafkaEventService = kafkaEventService;
        this.processedNotificationRepository = processedNotificationRepository;
        this.kafkaMapper = kafkaMapper;
    }

    @Transactional
    @KafkaHandler
    void handler(
            @Payload EventChangerEvent event,
            @Header("messageId") String messageId
    ) {
        LOGGER.info("Received event from kafka: {}", event);
        if (processedNotificationRepository.existsByMessageId(messageId)) {
            LOGGER.error("Duplicate message id: {}", messageId);
            return;
        }

        var changedEvent = kafkaMapper.toChangedEvent(event);
        kafkaEventService.addAll(changedEvent, event.getEventSubscribers());

        try {
            processedNotificationRepository.save(new IdempotencyMessageEntity(
                    null,
                    event.getEventId(),
                    event.getChangedEventByUserId(),
                    messageId,
                    LocalDateTime.now()
            ));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Error while saving message id = {}", e.getMessage());
            throw new NotRetryableException(e);
        }
    }
}

package dev.sorokin.event.notificator.domain.service;

import dev.sorokin.event.notificator.db.entity.EventFieldsEntity;
import dev.sorokin.event.notificator.db.repository.NotificationsRepository;
import dev.sorokin.event.notificator.domain.NotificationChangedEvent;
import dev.sorokin.event.notificator.mapper.EventFieldsMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationsRepository notificationsRepository;
    private final EventFieldsService eventFieldsService;
    private final EventFieldsMapper eventFieldsMapper;

    public NotificationService(NotificationsRepository notificationsRepository, EventFieldsService eventFieldsService, EventFieldsMapper eventFieldsMapper) {
        this.notificationsRepository = notificationsRepository;
        this.eventFieldsService = eventFieldsService;
        this.eventFieldsMapper = eventFieldsMapper;
    }

    public List<NotificationChangedEvent> getUnreadNotificationsCurrentUser() {
        Long userId = getCurrentUserId();

        var notifications = notificationsRepository.findAllBySubscriberIdIsAndIsReadFalse(userId);

        return notifications.stream()
                .map(n -> {
                    EventFieldsEntity eventFields =
                            eventFieldsService.findLatestUpdatedEventFields(n.getFieldsEntity().getId());
                    return NotificationChangedEvent.builder()
                            .id(n.getId())
                            .eventId(n.getEventId())
                            .userChangedId(n.getUserChangedId())
                            .subscriberId(n.getSubscriberId())
                            .isRead(n.getIsRead())
                            .createdAt(n.getCreatedAt())
                            .newFields(eventFieldsMapper.toDomain(eventFields))
                            .oldFields(eventFieldsMapper.toDomain(n.getFieldsEntity()))
                            .build();
                })
                .toList();
    }

    @Transactional
    public void markNotificationsAsRead(List<Long> notificationIds) {
        Long userId = getCurrentUserId();
        notificationIds
                .forEach(notificationId -> {
                    notificationsRepository.updateIsReadAsTrueByUserIdAndId(userId, notificationId);
                });
        LOGGER.info("Notifications are Updated As Read ={}", notificationIds);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("Authentication required");
        }

        return Long.parseLong(auth.getName());
    }
}
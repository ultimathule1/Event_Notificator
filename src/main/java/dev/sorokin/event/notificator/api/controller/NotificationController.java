package dev.sorokin.event.notificator.api.controller;

import dev.sorokin.event.notificator.api.dto.MarkAsReadNotificationsDto;
import dev.sorokin.event.notificator.api.dto.UserEventChangesDto;
import dev.sorokin.event.notificator.domain.NotificationChangedEvent;
import dev.sorokin.event.notificator.domain.service.NotificationService;
import dev.sorokin.event.notificator.mapper.NotificationChangedEventRespMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;
    private final NotificationChangedEventRespMapper changedEventMapper;


    public NotificationController(NotificationService notificationService, NotificationChangedEventRespMapper changedEventMapper) {
        this.notificationService = notificationService;
        this.changedEventMapper = changedEventMapper;
    }

    @GetMapping
    ResponseEntity<List<UserEventChangesDto>> getUnreadNotificationsCurrentUser() {
        LOGGER.info("Received request to get unread notifications for current user");
        List<NotificationChangedEvent> changedEvents = notificationService.getUnreadNotificationsCurrentUser();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(changedEvents
                        .stream()
                        .map(changedEventMapper::toDto)
                        .toList()
                );
    }

    @PostMapping
    ResponseEntity<Void> markNotificationAsRead(
            @RequestBody MarkAsReadNotificationsDto notificationsDto
    ) {
        LOGGER.info("Received request to mark notifications as read = {}", notificationsDto);
        notificationService.markNotificationsAsRead(notificationsDto.getNotificationIds());
        return ResponseEntity
                .noContent()
                .build();
    }
}

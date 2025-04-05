package dev.sorokin.event.notificator.config;

import dev.sorokin.event.notificator.db.repository.NotificationsRepository;
import dev.sorokin.event.notificator.db.repository.ProcessedNotificationRepository;
import dev.sorokin.event.notificator.domain.service.EventFieldsService;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class SchedulerConfig {

    private static final Logger LOGGER = Logger.getLogger(SchedulerConfig.class.getName());
    private final NotificationsRepository notificationsRepository;
    private final ProcessedNotificationRepository processedNotificationRepository;
    private final EventFieldsService eventFieldsService;

    public SchedulerConfig(NotificationsRepository notificationsRepository, ProcessedNotificationRepository processedNotificationRepository, EventFieldsService eventFieldsService) {
        this.notificationsRepository = notificationsRepository;
        this.processedNotificationRepository = processedNotificationRepository;
        this.eventFieldsService = eventFieldsService;
    }

    /**
     * Soft delete notifications that have existed for more than a week.
     * Where NotificationChangedEventEntity entities are removed completely,
     * but EventFields remain and are recorded in EventFieldsAudit as removed.
     * Now these entities cannot be changed.
     */
    @Scheduled(cron = "${scheduler.interval.cron.every-ten-minutes}")
    @Transactional
    public void schedulerForRemoveNotifications() {
        LOGGER.info("The scheduler for removing notifications for more than seven days is launched!");

        List<Long> notificationsDeletedList =
                notificationsRepository.deleteAllForLatestSevenDays();

        HashSet<Long> removedEntitiesId = new HashSet<>(notificationsDeletedList);

        eventFieldsService.removeAll(removedEntitiesId.stream().toList(), null);

        LOGGER.info("The scheduler for removing notifications for more than seven days is finished!");
    }

    /**
     * The complete deletion of the essence that was removed more than a month ago,
     * as well as the deletion of records from the audit.
     */
    @Scheduled(cron = "${scheduler.interval.cron.every-hour}")
    public void schedulerForDeleteNotifications() {
        LOGGER.info("The scheduler for deleting notifications for more than a month is launched!");
        eventFieldsService.deleteAllRemovableEntitiesWhoExpiredForMoreMonth();
        LOGGER.info("The scheduler for deleting notifications for more than a month is finished!");
    }

    @Scheduled(cron = "${scheduler.interval.cron.every-hour}")
    public void schedulerForRemoveOldIdempotentMessages() {
        LOGGER.info("The scheduler for removing idempotency messages for more than seven days is launched!");
        processedNotificationRepository.deleteAllForLatestSevenDays();
        LOGGER.info("The scheduler for removing idempotency messages for more than seven days is finished!");
    }
}

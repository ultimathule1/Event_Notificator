package dev.sorokin.event.notificator.domain;

import java.time.LocalDateTime;

public record NotificationChangedEvent(
        Long id,
        Long subscriberId,
        Long userChangedId,
        Long eventId,
        Long ownerEventId,
        LocalDateTime createdAt,
        Boolean isRead,
        EventFields newFields,
        EventFields oldFields
) {
    public static NotificationChangedEventBuilder builder() {
        return new NotificationChangedEventBuilder();
    }

    public static class NotificationChangedEventBuilder {
        private Long subscriberId;
        private Long userChangedId;
        private Long eventId;
        private Long ownerEventId;
        private LocalDateTime createdAt;
        private Boolean isRead;
        private EventFields newFields;
        private EventFields oldFields;

        public NotificationChangedEventBuilder id(Long id) {
            this.subscriberId = id;
            return this;
        }

        public NotificationChangedEventBuilder subscriberId(Long subscriberId) {
            this.subscriberId = subscriberId;
            return this;
        }

        public NotificationChangedEventBuilder userChangedId(Long userChangedId) {
            this.userChangedId = userChangedId;
            return this;
        }

        public NotificationChangedEventBuilder eventId(Long eventId) {
            this.eventId = eventId;
            return this;
        }

        public NotificationChangedEventBuilder ownerEventId(Long ownerEventId) {
            this.ownerEventId = ownerEventId;
            return this;
        }

        public NotificationChangedEventBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NotificationChangedEventBuilder isRead(Boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public NotificationChangedEventBuilder newFields(EventFields newFields) {
            this.newFields = newFields;
            return this;
        }

        public NotificationChangedEventBuilder oldFields(EventFields oldFields) {
            this.oldFields = oldFields;
            return this;
        }

        public NotificationChangedEvent build() {
            return new NotificationChangedEvent(
                    null,
                    subscriberId,
                    userChangedId,
                    eventId,
                    ownerEventId,
                    createdAt,
                    isRead,
                    newFields,
                    oldFields
            );
        }
    }
}
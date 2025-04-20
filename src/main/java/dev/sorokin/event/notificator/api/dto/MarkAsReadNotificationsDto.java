package dev.sorokin.event.notificator.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MarkAsReadNotificationsDto {
    List<Long> notificationIds;
}

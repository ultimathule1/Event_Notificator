package dev.sorokin.event.notificator.api.dto;

import java.time.LocalDateTime;

public record ErrorMessageDto(
        String message,
        String detailMessage,
        LocalDateTime dateTime
) {
}
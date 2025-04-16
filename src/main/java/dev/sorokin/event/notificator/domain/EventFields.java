package dev.sorokin.event.notificator.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record EventFields(
        Long id,
        String name,
        Integer maxPlaces,
        OffsetDateTime date,
        ZoneOffset offsetDate,
        BigDecimal cost,
        Integer duration,
        Long locationId,
        String status
) {
}
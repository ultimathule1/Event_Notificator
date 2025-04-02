package dev.sorokin.event.notificator.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record EventFields(
        Long id,
        String name,
        Integer maxPlaces,
        OffsetDateTime date,
        BigDecimal cost,
        Integer duration,
        Long locationId,
        String status
) {
}
package dev.sorokin.event.notificator.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ChangedEvent(
        Long changedEventByUserId,
        Long eventId,
        Long ownerEventId,
        String nameOld,
        String nameNew,
        Integer maxPlacesOld,
        Integer maxPlacesNew,
        OffsetDateTime dateOld,
        OffsetDateTime dateNew,
        BigDecimal costOld,
        BigDecimal costNew,
        Integer durationOld,
        Integer durationNew,
        Long locationIdOld,
        Long locationIdNew,
        String statusOld,
        String statusNew
) {
}

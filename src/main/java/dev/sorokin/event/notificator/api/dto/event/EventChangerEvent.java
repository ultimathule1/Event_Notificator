package dev.sorokin.event.notificator.api.dto.event;

import dev.sorokin.event.notificator.api.dto.FieldChange;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventChangerEvent {
    @NotNull
    @Positive
    private Long eventId;
    private Long changedEventByUserId;
    @NotNull
    @Positive
    private Long ownerEventId;
    @NotNull
    private List<Long> eventSubscribers;

    private FieldChange<String> fieldEventName;
    private FieldChange<BigDecimal> fieldEventCost;
    private FieldChange<OffsetDateTime> fieldEventDate;
    private ZoneOffset offsetDate;
    private FieldChange<Integer> fieldMaxPlaces;
    private FieldChange<Integer> fieldDuration;
    private FieldChange<Long> fieldLocationId;
    private FieldChange<String> fieldStatus;
}
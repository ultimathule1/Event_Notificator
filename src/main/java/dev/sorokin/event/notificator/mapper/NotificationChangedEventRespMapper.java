package dev.sorokin.event.notificator.mapper;

import dev.sorokin.event.notificator.api.dto.UserEventChangesDto;
import dev.sorokin.event.notificator.domain.NotificationChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NotificationChangedEventRespMapper {
    @Mapping(target = "fieldChangeName.oldValue", source = "oldFields.name")
    @Mapping(target = "fieldChangeName.newValue", source = "newFields.name")
    @Mapping(target = "fieldChangeMaxPlaces.oldValue", source = "oldFields.maxPlaces")
    @Mapping(target = "fieldChangeMaxPlaces.newValue", source = "newFields.maxPlaces")
    @Mapping(target = "fieldChangeDate.oldValue", source = "oldFields.date")
    @Mapping(target = "fieldChangeDate.newValue", source = "newFields.date")
    @Mapping(target = "fieldChangeCost.oldValue", source = "oldFields.cost")
    @Mapping(target = "fieldChangeCost.newValue", source = "newFields.cost")
    @Mapping(target = "fieldChangeDuration.oldValue", source = "oldFields.duration")
    @Mapping(target = "fieldChangeDuration.newValue", source = "newFields.duration")
    @Mapping(target = "fieldChangeLocationId.oldValue", source = "oldFields.locationId")
    @Mapping(target = "fieldChangeLocationId.newValue", source = "newFields.locationId")
    @Mapping(target = "fieldChangeStatus.oldValue", source = "oldFields.status")
    @Mapping(target = "fieldChangeStatus.newValue", source = "newFields.status")
    UserEventChangesDto toDto(NotificationChangedEvent notificationChangedEvent);
}

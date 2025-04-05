package dev.sorokin.event.notificator.mapper;

import dev.sorokin.event.notificator.api.dto.FieldChange;
import dev.sorokin.event.notificator.api.dto.event.EventChangerEvent;
import dev.sorokin.event.notificator.domain.ChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EventKafkaMapper {

    @Mapping(target = "nameOld", source = "fieldEventName", qualifiedByName = "extractOld")
    @Mapping(target = "nameNew", source = "fieldEventName", qualifiedByName = "extractNew")
    @Mapping(target = "maxPlacesOld", source = "fieldMaxPlaces", qualifiedByName = "extractOld")
    @Mapping(target = "maxPlacesNew", source = "fieldMaxPlaces", qualifiedByName = "extractNew")
    @Mapping(target = "dateOld", source = "fieldEventDate", qualifiedByName = "extractOld")
    @Mapping(target = "dateNew", source = "fieldEventDate", qualifiedByName = "extractNew")
    @Mapping(target = "costOld", source = "fieldEventCost", qualifiedByName = "extractOld")
    @Mapping(target = "costNew", source = "fieldEventCost", qualifiedByName = "extractNew")
    @Mapping(target = "durationOld", source = "fieldDuration", qualifiedByName = "extractOld")
    @Mapping(target = "durationNew", source = "fieldDuration", qualifiedByName = "extractNew")
    @Mapping(target = "locationIdOld", source = "fieldLocationId", qualifiedByName = "extractOld")
    @Mapping(target = "locationIdNew", source = "fieldLocationId", qualifiedByName = "extractNew")
    @Mapping(target = "statusOld", source = "fieldStatus", qualifiedByName = "extractOld")
    @Mapping(target = "statusNew", source = "fieldStatus", qualifiedByName = "extractNew")
    ChangedEvent toChangedEvent(EventChangerEvent eventChangerEvent);

    @Named("extractOld")
    default <T> T extractOld(FieldChange<T> fieldChange) {
        return fieldChange == null ? null : fieldChange.getOldValue();
    }

    @Named("extractNew")
    default <T> T extractNew(FieldChange<T> fieldChange) {
        return fieldChange == null ? null : fieldChange.getNewValue();
    }
}
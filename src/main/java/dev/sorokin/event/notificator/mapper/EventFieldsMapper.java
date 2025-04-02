package dev.sorokin.event.notificator.mapper;

import dev.sorokin.event.notificator.db.entity.EventFieldsEntity;
import dev.sorokin.event.notificator.domain.EventFields;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EventFieldsMapper {
    EventFields toDomain(EventFieldsEntity eventFieldsEntity);
}

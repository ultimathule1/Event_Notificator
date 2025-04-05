package dev.sorokin.event.notificator.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.sorokin.event.notificator.api.dto.serializer.FieldChangeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonSerialize(using = FieldChangeSerializer.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FieldChange<T> {
    private T oldValue;
    private T newValue;
}
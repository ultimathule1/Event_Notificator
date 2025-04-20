package dev.sorokin.event.notificator.api.dto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.sorokin.event.notificator.api.dto.FieldChange;

import java.io.IOException;

public class FieldChangeSerializer extends JsonSerializer<FieldChange<?>> {
    @Override
    public void serialize(FieldChange<?> fieldChange, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("oldValue", fieldChange.getOldValue());
        gen.writeObjectField("newValue", fieldChange.getNewValue());
        gen.writeEndObject();
    }

    @Override
    public boolean isEmpty(SerializerProvider provider, FieldChange<?> fieldChange) {
        return fieldChange.getOldValue() == null && fieldChange.getNewValue() == null;
    }
}

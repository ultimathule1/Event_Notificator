package dev.sorokin.event.notificator.db.converter;

import jakarta.persistence.AttributeConverter;

import java.time.ZoneOffset;

public class ZoneOffsetConverter implements AttributeConverter<ZoneOffset, String> {
    @Override
    public String convertToDatabaseColumn(ZoneOffset offset) {
        if (offset == null) {
            return null;
        }

        String offsetCurrent = offset.toString();

        return offsetCurrent.equals("Z") ? "+00:00" : offsetCurrent;
    }

    @Override
    public ZoneOffset convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }

        return ZoneOffset.of(s);
    }
}

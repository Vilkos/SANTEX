package com.santex.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static com.santex.entity.Status.*;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, Character> {

    @Override
    public Character convertToDatabaseColumn(Status attribute) {
        switch (attribute) {
            case Виконано:
                return 'Y';
            case Нове:
                return 'N';
            case Обробляється:
                return 'P';
            default:
                throw new IllegalArgumentException("Unknown" + attribute);
        }
    }

    @Override
    public Status convertToEntityAttribute(Character dbData) {
        switch (dbData) {
            case 'Y':
                return Виконано;
            case 'N':
                return Нове;
            case 'P':
                return Обробляється;
            default:
                throw new IllegalArgumentException("Unknown" + dbData);
        }
    }
}

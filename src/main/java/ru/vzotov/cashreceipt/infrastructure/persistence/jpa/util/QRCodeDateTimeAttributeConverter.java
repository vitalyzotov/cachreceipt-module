package ru.vzotov.cashreceipt.infrastructure.persistence.jpa.util;

import ru.vzotov.cashreceipt.domain.model.QRCodeDateTime;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;

public class QRCodeDateTimeAttributeConverter implements AttributeConverter<QRCodeDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(QRCodeDateTime localDateTime) {
        return (localDateTime == null ? null : Timestamp.valueOf(localDateTime.value()));
    }

    @Override
    public QRCodeDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        return (sqlTimestamp == null ? null : new QRCodeDateTime(sqlTimestamp.toLocalDateTime()));
    }
}

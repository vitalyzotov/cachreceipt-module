package ru.vzotov.cashreceipt.domain.model;

import java.time.LocalDate;
import java.util.List;

public interface CheckRepository {
    Check find(CheckId id);

    Check findByQRCodeData(QRCodeData data);

    void store(Check check);

    List<Check> findByDate(LocalDate fromDate, LocalDate toDate);
    long countByDate(LocalDate fromDate, LocalDate toDate);

    Check findOldest();
}

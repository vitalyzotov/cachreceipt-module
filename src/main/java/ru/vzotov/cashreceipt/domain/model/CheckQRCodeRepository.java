package ru.vzotov.cashreceipt.domain.model;

import java.time.LocalDate;
import java.util.List;

public interface CheckQRCodeRepository {
    CheckQRCode find(CheckId id);

    CheckQRCode findByQRCodeData(QRCodeData data);

    void store(CheckQRCode check);

    List<CheckQRCode> findByDate(LocalDate fromDate, LocalDate toDate);

    List<CheckQRCode> findAllInState(CheckState state);

}

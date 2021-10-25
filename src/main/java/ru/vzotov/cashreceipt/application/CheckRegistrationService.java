package ru.vzotov.cashreceipt.application;

import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;

import java.io.IOException;

public interface CheckRegistrationService {

    /**
     * Регистрация чека в системе
     *
     * @param qrCodeData сведения QR-кода
     * @return идентификатор QR кода
     */
    CheckId registerCheck(QRCodeData qrCodeData) throws CheckNotFoundException, IOException;

    /**
     * Инициирует сбор информации о чеке из внешних систем, например, из налоговой службы.
     *
     * @param qrCodeData сведения из QR-кода
     * @return идентификатор чека
     * @throws CheckNotFoundException если чек не был найден
     * @throws IOException            при ошибках ввода-вывода
     */
    CheckId loadCheckDetails(QRCodeData qrCodeData) throws CheckNotFoundException, IOException;

    void loadNewChecks();

}

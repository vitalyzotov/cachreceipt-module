package ru.vzotov.cashreceipt.interfaces.accounting.facade.impl.assembler;

import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.QRCodeDataDTO;
import ru.vzotov.cashreceipt.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.cashreceipt.interfaces.common.assembler.MoneyDTOAssembler;

public class QRCodeDataDTOAssembler extends AbstractAssembler<QRCodeDataDTO, QRCodeData> {

    public QRCodeDataDTO toDTO(QRCodeData data) {
        MoneyDTOAssembler money = new MoneyDTOAssembler();
        return new QRCodeDataDTO(
                data.dateTime().value(),
                money.toDTO(data.totalSum()),
                data.fiscalDriveNumber(),
                data.fiscalDocumentNumber(),
                data.fiscalSign().toString(),
                data.operationType()
        );
    }

}

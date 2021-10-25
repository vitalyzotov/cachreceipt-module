package ru.vzotov.cashreceipt.interfaces.accounting.facade.impl.assembler;

import ru.vzotov.cashreceipt.domain.model.CheckQRCode;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.cashreceipt.interfaces.common.assembler.AbstractAssembler;

public class QRCodeDTOAssembler extends AbstractAssembler<QRCodeDTO, CheckQRCode> {

    private final QRCodeDataDTOAssembler assembler = new QRCodeDataDTOAssembler();

    @Override
    public QRCodeDTO toDTO(CheckQRCode code) {
        return code == null ? null : new QRCodeDTO(
                code.checkId().value(),
                assembler.toDTO(code.code()),
                code.state().name(),
                code.loadingTryCount(),
                code.loadedAt()
        );
    }

}

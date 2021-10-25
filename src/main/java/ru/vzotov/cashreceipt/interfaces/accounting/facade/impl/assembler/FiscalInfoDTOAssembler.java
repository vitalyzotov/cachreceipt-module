package ru.vzotov.cashreceipt.interfaces.accounting.facade.impl.assembler;

import ru.vzotov.cashreceipt.domain.model.FiscalInfo;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.FiscalInfoDTO;
import ru.vzotov.cashreceipt.interfaces.common.assembler.AbstractAssembler;

public class FiscalInfoDTOAssembler extends AbstractAssembler<FiscalInfoDTO, FiscalInfo> {

    @Override
    public FiscalInfoDTO toDTO(FiscalInfo fiscalInfo) {
        if (fiscalInfo == null) return null;
        return new FiscalInfoDTO(
                fiscalInfo.kktNumber(),
                fiscalInfo.kktRegId(),
                String.valueOf(fiscalInfo.fiscalSign().value()),
                fiscalInfo.fiscalDocumentNumber(),
                fiscalInfo.fiscalDriveNumber()
        );
    }
}

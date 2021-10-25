package ru.vzotov.cashreceipt.interfaces.accounting.rest.dto;

import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.CheckDTO;

import java.util.List;

public class GetChecksResponse {
    private List<CheckDTO> checks;

    private List<CheckDTO> qrCodes;

    public GetChecksResponse() {
    }

    public GetChecksResponse(List<CheckDTO> checks) {
        this.checks = checks;
    }

    public List<CheckDTO> getChecks() {
        return checks;
    }

}

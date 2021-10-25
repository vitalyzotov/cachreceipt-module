package ru.vzotov.cashreceipt.interfaces.accounting.rest.dto;

import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.CheckDTO;

public class GetCheckResponse {
    private CheckDTO check;

    public GetCheckResponse() {
    }

    public GetCheckResponse(CheckDTO check) {
        this.check = check;
    }

    public CheckDTO getCheck() {
        return check;
    }

    public void setCheck(CheckDTO check) {
        this.check = check;
    }
}

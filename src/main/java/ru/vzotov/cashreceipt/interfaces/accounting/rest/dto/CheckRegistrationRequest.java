package ru.vzotov.cashreceipt.interfaces.accounting.rest.dto;

@SuppressWarnings("unused")
public class CheckRegistrationRequest {

    private String qrcode;

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}

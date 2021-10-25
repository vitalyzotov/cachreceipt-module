package ru.vzotov.cashreceipt.interfaces.accounting.rest.dto;

@SuppressWarnings("unused")
public class CheckRegistrationResponse {

    private String id;

    public CheckRegistrationResponse() {
    }

    public CheckRegistrationResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

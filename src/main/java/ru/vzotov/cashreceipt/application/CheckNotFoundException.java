package ru.vzotov.cashreceipt.application;

public class CheckNotFoundException extends Exception {

    @SuppressWarnings("WeakerAccess")
    public CheckNotFoundException() {
    }

    public CheckNotFoundException(String message) {
        super(message);
    }

    public CheckNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}

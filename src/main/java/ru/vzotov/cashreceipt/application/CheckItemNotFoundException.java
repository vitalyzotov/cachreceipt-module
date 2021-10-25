package ru.vzotov.cashreceipt.application;

public class CheckItemNotFoundException extends Exception {

    @SuppressWarnings("WeakerAccess")
    public CheckItemNotFoundException() {
    }

    public CheckItemNotFoundException(String message) {
        super(message);
    }

    public CheckItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}

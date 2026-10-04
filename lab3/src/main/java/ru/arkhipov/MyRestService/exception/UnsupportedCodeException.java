package ru.arkhipov.MyRestService.exception;

public class UnsupportedCodeException extends RuntimeException {

    public UnsupportedCodeException(String message) {
        super(message);
    }
}

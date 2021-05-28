package br.com.capgemini.rogersilva.unittest.exception;

public class BadRequestException extends Exception {

    private static final long serialVersionUID = -5947835112928798783L;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
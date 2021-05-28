package br.com.capgemini.rogersilva.unittest.exception;

public class NotFoundException extends Exception {

    private static final long serialVersionUID = 6928295201208205491L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
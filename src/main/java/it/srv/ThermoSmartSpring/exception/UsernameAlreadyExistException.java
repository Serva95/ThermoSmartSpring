package it.srv.ThermoSmartSpring.exception;

public class UsernameAlreadyExistException extends Exception {

    public UsernameAlreadyExistException (final String message) {
        super(message);
    }
}

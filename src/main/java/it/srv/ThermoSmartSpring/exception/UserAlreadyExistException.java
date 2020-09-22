package it.srv.ThermoSmartSpring.exception;

public class UserAlreadyExistException extends Throwable {

    public UserAlreadyExistException (final String message) {
        super(message);
    }
}

package org.pineapple.stateMachine.Exception;

public class InvalidPOP3CommandException extends RuntimeException {

    public InvalidPOP3CommandException(String errorMessage) {
        super(errorMessage);
    }
    public InvalidPOP3CommandException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

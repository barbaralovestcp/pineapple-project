package org.pineapple.stateMachine.Exception;

public class InvalidCommandException extends RuntimeException {

    public InvalidCommandException(String errorMessage) {
        super(errorMessage);
    }
    public InvalidCommandException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

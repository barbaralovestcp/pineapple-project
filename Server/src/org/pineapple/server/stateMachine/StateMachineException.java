package org.pineapple.server.stateMachine;

public class StateMachineException extends RuntimeException {

    public StateMachineException(String errorMessage) {
        super(errorMessage);
    }
    public StateMachineException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

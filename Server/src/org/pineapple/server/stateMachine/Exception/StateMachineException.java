package org.pineapple.server.stateMachine.Exception;

import org.pineapple.server.stateMachine.*;

public class StateMachineException extends RuntimeException {

    /*
    public StateMachineException(String errorMessage) {
        super(errorMessage);
    }
    public StateMachineException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    } */

    public StateMachineException(State state, CommandPOP3 command) {
        super("Unhandled POP3 entry \'" + command.toString() + "\' in the state \'" + state.getClass().getSimpleName() + "\'");
    }
}

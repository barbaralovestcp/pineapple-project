package org.pineapple.stateMachine.Exception;

import org.pineapple.ICommand;
import org.pineapple.stateMachine.*;

public class StateMachineException extends RuntimeException {


    public StateMachineException(IState state, ICommand command) {
        super("Unhandled Command entry \'" + command.toString() + "\' in the state \'" + state.getClass().getSimpleName() + "\'");
    }
}

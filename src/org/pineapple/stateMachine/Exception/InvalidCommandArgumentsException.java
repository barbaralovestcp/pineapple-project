package org.pineapple.stateMachine.Exception;

import org.pineapple.ICommand;

public class InvalidCommandArgumentsException extends RuntimeException {

    public InvalidCommandArgumentsException(ICommand command, String msg) {
        super("Invalid Command Arguments for \'" + command.toString() + "\'. " + msg);
    }
}

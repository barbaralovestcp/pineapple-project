package org.pineapple.stateMachine.Exception;

import org.pineapple.CommandPOP3;

public class InvalidPOP3ArgumentsException extends RuntimeException {

    public InvalidPOP3ArgumentsException(CommandPOP3 command, String msg) {
        super("Invalid POP3 Arguments for \'" + command.toString() + "\'. " + msg);
    }
}

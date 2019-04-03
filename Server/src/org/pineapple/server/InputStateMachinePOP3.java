package org.pineapple.server;

import org.jetbrains.annotations.NotNull;
import org.pineapple.CommandPOP3;
import org.pineapple.stateMachine.AbstractInputStateMachine;
import org.pineapple.stateMachine.Exception.InvalidCommandException;
import org.pineapple.stateMachine.IInputStateMachine;

/**
 * The input of the state machine, it contains the CommandPOP3 enum + optional strings.
 */
public class InputStateMachinePOP3 extends AbstractInputStateMachine<CommandPOP3> implements IInputStateMachine {

    /**
     * Create a InputStateMachinePOP3 from a POP3 request. Throws an error if it's not a valid POP3 command.
     * @param request
     */
    public InputStateMachinePOP3(String request) {
        super(request, CommandPOP3.class);
    }

    //region Static Methods

    /**
     * Return true if the request is a Valid pop3 command
     * @param request
     * @return
     */
    public static boolean isValidRequest(@NotNull String request) {
        return isValidRequest(request, CommandPOP3.class);
    }
}

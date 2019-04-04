package org.pineapple.serversmtp;

import org.jetbrains.annotations.NotNull;
import org.pineapple.CommandPOP3;
import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.AbstractInputStateMachine;
import org.pineapple.stateMachine.Exception.InvalidCommandException;
import org.pineapple.stateMachine.IInputStateMachine;

public class InputStateMachineSMTP extends AbstractInputStateMachine<CommandSMTP> implements IInputStateMachine {

    public InputStateMachineSMTP(@NotNull String request) {
        super(request, CommandSMTP.class);
    }
    
    public static boolean isValidRequest(@NotNull String request) {
        return isValidRequest(request, CommandSMTP.class);
    }
}

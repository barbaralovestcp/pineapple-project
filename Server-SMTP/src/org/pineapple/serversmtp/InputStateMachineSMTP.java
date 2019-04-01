package org.pineapple.serversmtp;

import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.IInputStateMachine;

public class InputStateMachineSMTP implements IInputStateMachine {


    private CommandSMTP command;
    private String[] arguments;

    public CommandSMTP getCommand() {
        return command;
    }

    public String[] getArguments() {
        return arguments;
    }
}

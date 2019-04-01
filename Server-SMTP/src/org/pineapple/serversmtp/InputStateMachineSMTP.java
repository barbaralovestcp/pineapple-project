package org.pineapple.serversmtp;

import org.jetbrains.annotations.NotNull;
import org.pineapple.CommandPOP3;
import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.Exception.InvalidCommandException;
import org.pineapple.stateMachine.IInputStateMachine;

public class InputStateMachineSMTP implements IInputStateMachine {


    private CommandSMTP command;
    private String[] arguments;
    
    public InputStateMachineSMTP(@NotNull String request) {
        this.arguments = request.split("\\s+");
        try {
            this.command = CommandSMTP.valueOf(arguments[0].toUpperCase());
        }
        catch (IllegalArgumentException err) {
            throw new InvalidCommandException("Invalid SMTP Command '" + arguments[0] + "'", err);
        }
    }
    
    public static boolean IsValidSMTPRequest(@NotNull String request) {
        String[] args = request.split("\\s+");
        try {
            CommandSMTP comm = CommandSMTP.valueOf(args[0].toUpperCase());
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }

    public CommandSMTP getCommand() {
        return command;
    }

    public String[] getArguments() {
        return arguments;
    }
}

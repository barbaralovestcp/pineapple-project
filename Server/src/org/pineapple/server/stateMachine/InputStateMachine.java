package org.pineapple.server.stateMachine;

import org.pineapple.CommandPOP3;
import org.pineapple.server.stateMachine.Exception.InvalidPOP3CommandException;

/**
 * The input of the state machine, it contains the CommandPOP3 enum + optional strings.
 */
public class InputStateMachine {

    private CommandPOP3 command;
    private String[] arguments;


    /**
     * Create a InputStateMachine from a POP3 request. Throws an error if it's not a valid POP3 command.
     * @param request
     */
    public InputStateMachine(String request) {

        this.arguments = request.split("\\s+");
        try {
            this.command = CommandPOP3.valueOf(arguments[0].toUpperCase());
        }
        catch (IllegalArgumentException err) {
            throw new InvalidPOP3CommandException("Invalid POP3 Command " + "\'" + arguments[0] + "\'", err);
        }
    }

    public InputStateMachine(CommandPOP3 command, String[] arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    //Region Static Methods

    /**
     * Return true if the request is a Valid pop3 command
     * @param request
     * @return
     */
    public static boolean IsValidPOP3Request(String request) {

        String[] args = request.split("\\s+");
        try {
            CommandPOP3 comm = CommandPOP3.valueOf(args[0].toUpperCase());
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }

    //endregion

    //region Getter Setter
    public CommandPOP3 getCommand() {
        return command;
    }

    public String[] getArguments() {
        return arguments;
    }
    //endregion
}

package org.pineapple.clientsmtp.stateMachine;

import org.pineapple.ICommand;
import org.pineapple.stateMachine.IInputStateMachine;

public class InputStateMachineClient implements IInputStateMachine {

    private String[] arguments;

    public InputStateMachineClient(String[] args) {
        this.arguments = args;
    }

    public InputStateMachineClient(String message) {
        this.arguments = message.split("\\s+");
    }

    @Override
    public String[] getArguments() {
        return arguments;
    }

    @Override
    public ICommand getCommand() {
        System.out.println("WARNING : Input State Machine doesn't use commands!");
        return null;
    }

    public static boolean isValidCommand(String request){
        String[] arguments = request.split(" ");
        return arguments.length > 0  && (arguments[0].equals("250") || arguments[0].equals("220") ||
                arguments[0].equals("354") ||arguments[0].equals("550"));
    }
}

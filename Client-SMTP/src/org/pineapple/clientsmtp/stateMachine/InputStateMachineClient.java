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
}

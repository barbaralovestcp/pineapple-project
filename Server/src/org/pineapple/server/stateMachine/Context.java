package org.pineapple.server.stateMachine;

import java.sql.SQLOutput;

public class Context {

    private State currentState;

    //TODO : Give an attribute to interact with the server : Send message. Will be used by State when processing.

    public Context(State initialState) {
        currentState = initialState;
        currentState.onStateEntry(this);
    }

    public Context() {
        this(new StateServerListening());
    }

    /*
        A state will need for handling :
            - A CommandPOP3 (APOP, USER, QUIT...)
            - Optional arguments as String (depending on CommandPOP3)
            - Access to a Server interface to complete its treatment by sending messages (?).

        Obtaining the entry :
            The server will receive a String message from the Client.
            That String will need to be processed to get CommandPOP3 + Args.

            I don't know yet where's the best place to put this process
            (split the received string into enum + string), make the server handle it and pass
            it to the StateMachine, or give the StateMachine/State the responsability to process it ?
     */

    public void handle(InputStateMachine input) {
        handle(input.getCommand(), input.getArguments());
    }
    public void handle(final CommandPOP3 entry, final String[] args) {

        try {
            currentState.handle(this, entry, args);
            System.out.println("Successful transition (" + entry.toString() + "), new state : " + currentState.getClass().getSimpleName());
        }
        catch (StateMachineException err) {
            System.out.println("ERR : Unhandled input (" + entry.toString() + ") on current state (" + currentState.getClass().getSimpleName()+")");
        }
    }

    /**
     * Set the new State of the State Machine.
     * Should only be used by States, when finishing their treatment.
     * @param newState next state of the stateMachine
     */
    public void setState(State newState) {
        currentState = newState;
        currentState.onStateEntry(this);
    }


}

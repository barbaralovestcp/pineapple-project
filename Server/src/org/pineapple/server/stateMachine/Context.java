package org.pineapple.server.stateMachine;

import org.pineapple.CommandPOP3;
import org.pineapple.server.stateMachine.Exception.InvalidPOP3CommandException;
import org.pineapple.server.stateMachine.Exception.StateMachineException;

public class Context {

    private State currentState;

    //TODO : Give an attribute to interact with the server : Send message. Will be used by States when processing commands.

    public Context(State initialState) {
        currentState = initialState;
    }

    public Context() {
        this(new StateServerListening());

        //We suppose a connection has already been made, so we immediately transition from Listening --> Authentification
        //when creating Context. (Might change?) ServerListenind.handle() doesn't use any arguments.
        handle(null, null);
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

    public void handle(String fullCommand) {

        InputStateMachine input;
        try {
            input = new InputStateMachine(fullCommand);
        } catch (InvalidPOP3CommandException err) {
            throw err;
        }

        handle(input);
    }
    public void handle(final CommandPOP3 entry, final String[] args) {

        //currentState.handle(this, entry, args);

        try {
            currentState.handle(this, entry, args);
        }
        catch (StateMachineException err) {
            System.out.println("ERROR : " + err.getMessage() );
        }
    }

    /**
     * Set the new State of the State Machine.
     * Should only be used by States, when finishing their treatment.
     * @param newState next state of the stateMachine
     */
    public void setState(State newState) {

        System.out.println("Successful State Transition : " + currentState.getClass().getSimpleName() + " ---> " + newState.getClass().getSimpleName());
        currentState = newState;
    }
    
    public State getCurrentState() {
        return currentState;
    }
}

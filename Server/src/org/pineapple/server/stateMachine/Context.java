package org.pineapple.server.stateMachine;

public class Context {

    private State currentState;

    public Context(State initialState) {
        currentState = initialState;
    }

    /*
        A state will need for handling :
            - A Command (APOP, USER, QUIT...)
            - Optional arguments as String (depending on Command)
            - Access to a Server interface to complete its treatment by sending messages (?).

        Obtaining the entry :
            The server will receive a String message from the Client.
            That String will need to be processed to get Command + Args.

            I don't know yet where's the best place to put this process, make the server handle it and pass
            it to the StateMachine, or give the StateMachine/State the responsability to process it ?
     */


    public void handle(Command entry, String arg) {
        currentState.handle(entry, arg);
    }

    /**
     * Set the new State of the State Machine.
     * Should only be used by States, when finishing their treatment.
     * @param newState next state of the stateMachine
     */
    public void setState(State newState) {
        currentState = newState;
    }


}

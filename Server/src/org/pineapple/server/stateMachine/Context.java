package org.pineapple.server.stateMachine;

import org.jetbrains.annotations.Nullable;
import org.pineapple.CommandPOP3;
import org.pineapple.MailBox;
import org.pineapple.server.stateMachine.Exception.InvalidPOP3CommandException;
import org.pineapple.server.stateMachine.Exception.StateMachineException;

import java.net.Socket;
import java.util.Observable;

public class Context {

    private State currentState;
    private String stateToLog;
    private boolean toQuit;

    private MailBox mailBox;

    @Nullable
    private String messageToSend;

    //TODO : Give an attribute to interact with the server : Send message. Will be used by States when processing commands.

    public Context(State initialState) {
        currentState = initialState;
        toQuit = false;
        messageToSend = null;
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
        String mess = "Successful State Transition : " + currentState.getClass().getSimpleName() + " ---> " + newState.getClass().getSimpleName();
        System.out.println(mess);
        this.setStateToLog(mess);
        currentState = newState;
    }
    
    public State getCurrentState() {
        return currentState;
    }
    
    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public MailBox getMailBox() { return this.mailBox; }

    public void setMailBox(MailBox mailbox) { this.mailBox = mailbox; }


    public String getStateToLog() {
        return this.stateToLog;
    }
    public void setStateToLog(String mess) {this.stateToLog = mess;}

    public boolean isToQuit() {
        return toQuit;
    }
    
    public void setToQuit(boolean toQuit) {
        this.toQuit = toQuit;
    }
    
    @Nullable
    public String getMessageToSend() {
        return messageToSend;
    }
    @Nullable
    public String popMessageToSend() {
        String message = getMessageToSend();
        setMessageToSend(null);
        return message;
    }
    
    public void setMessageToSend(@Nullable String messageToSend) {
        this.messageToSend = messageToSend;
    }
}

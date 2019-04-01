package org.pineapple.stateMachine;

import org.jetbrains.annotations.Nullable;
import org.pineapple.MailBox;
import org.pineapple.stateMachine.Exception.InvalidCommandArgumentsException;
import org.pineapple.stateMachine.Exception.StateMachineException;

public class Context {

    private IState currentState;
    private String stateToLog;
    private boolean toQuit;

    private MailBox mailBox;

    @Nullable
    private String messageToSend;

    public Context(IState initialState) {
        currentState = initialState;
        toQuit = false;
        messageToSend = null;
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
            it to the StateMachine, or give the StateMachine/IState the responsability to process it ?
     */

    //Server listening skipstep
    public void handle() {
        currentState.handle(this, null);
    }

    public void handle(IInputStateMachine input) {
        try {
            currentState.handle(this, input);
        }
        catch (StateMachineException | InvalidCommandArgumentsException err) {
            System.out.println("ERROR : " + err.getMessage() );
        }
    }

    /**
     * Set the new IState of the IState Machine.
     * Should only be used by States, when finishing their treatment.
     * @param newState next state of the stateMachine
     */
    public void setState(IState newState) {
        String mess = "Successful IState Transition : " + currentState.getClass().getSimpleName() + " ---> " + newState.getClass().getSimpleName();
        System.out.println(mess);
        this.setStateToLog(mess);
        currentState = newState;
    }
    
    public IState getCurrentState() {
        return currentState;
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

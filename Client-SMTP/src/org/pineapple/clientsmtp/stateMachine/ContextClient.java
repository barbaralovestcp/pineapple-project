package org.pineapple.clientsmtp.stateMachine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pineapple.Message;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.IState;

import java.util.ArrayList;
import java.util.HashMap;

public class ContextClient extends Context {

    @NotNull
    private String domain;
    @NotNull
    private String name;
    private Message message;
    private ArrayList<String> recipient;
    private int recipientIterator = 0;
    private int validIRecipient = 0;
    private HashMap<String,String> stateRecipient;

    public ContextClient() {
        super(new StateConnected());
    }

    public ContextClient(String name, String domain, IState initialState) {
        super(initialState);
        this.name = name;
        this.domain = domain;
        this.stateRecipient = new HashMap<>();
    }

    public void iterateRecipentSend() {
        this.recipientIterator++;
    }

    public void iterateRecipentValid() {
        this.validIRecipient++;
    }

    //ACCESSORS

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public ArrayList<String> getRecipient() {
        return recipient;
    }

    public void setRecipient(ArrayList<String> recipient) {
        this.recipient = recipient;
    }

    public int getRecipientIterator() {
        return recipientIterator;
    }

    public void setRecipientIterator(int recipientIterator) {
        this.recipientIterator = recipientIterator;
    }

    public int getValidIRecipient() {
        return validIRecipient;
    }

    public void setValidIRecipient(int validIRecipient) {
        this.validIRecipient = validIRecipient;
    }

    public HashMap<String, String> getStateRecipient() {
        return stateRecipient;
    }

    public void setStateRecipient(HashMap<String, String> stateRecipient) {
        this.stateRecipient = stateRecipient;
    }

    public String logStateRecipient(){
        String tolog = null;
        if(this.getStateRecipient().size() > 0){
            tolog = this.getStateRecipient().toString();
        }
        return tolog;
    }
}

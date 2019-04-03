package org.pineapple.clientsmtp.stateMachine;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.pineapple.Message;
import org.pineapple.stateMachine.Context;

import java.util.ArrayList;

public class ContextClient extends Context {

    //TODO : Il y a aura peut-être besoin de passer en Protected des attributs de Context.

    @NotNull
    private String domain;
    @NotNull
    private String name;
    @Nullable
    private Message message;
    private ArrayList<String> recipient;
    private int recipientIterator = 0;

    public ContextClient() {
        super(new StateConnected());
    }

    public ContextClient(String name, String domain) {
        super(new StateConnected());
        this.name = name;
        this.domain = domain;
    }

    public void handle(String request) {
        handle(new InputStateMachineClient(request));
    }

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

    public void iterate(){
        this.recipientIterator++;
    }
}

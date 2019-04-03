package org.pineapple.clientsmtp.stateMachine;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.pineapple.stateMachine.Context;

import java.util.ArrayList;

public class ContextClient extends Context {

    //TODO : Il y a aura peut-être besoin de passer en Protected des attributs de Context.

    @NotNull
    private String name;
    @Nullable
    private String message;
    private ArrayList<String> recipient;

    public ContextClient() {
        super(new StateConnected());
    }

    public ContextClient(String name) {
        super(new StateConnected());
    }

    public void handle(String request) {
        handle(new InputStateMachineClient(request));
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getRecipient() {
        return recipient;
    }

    public void setRecipient(ArrayList<String> recipient) {
        this.recipient = recipient;
    }
}

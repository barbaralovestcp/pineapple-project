package org.pineapple.clientsmtp;

import org.pineapple.stateMachine.Context;

public class ContextClient extends Context {

    //TODO : Il y a aura peut-être besoin de passer en Protected des attributs de Context.

    public ContextClient() {
        super(new StateConnected());
    }

    public void handle(String request) {
        handle(new InputStateMachineClient(request));
    }
}

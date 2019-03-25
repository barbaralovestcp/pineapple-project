package org.pineapple.server.stateMachine;

import org.pineapple.CodeOK;
import org.pineapple.CommandPOP3;

public class StateServerListening implements State {

    @Override
    public void handle(Context context, CommandPOP3 command, String[] args) {

        String toSend =  new CodeOK(CodeOK.CodeEnum.OK_SERVER_READY).toString("Server");

        System.out.println("toSend = " + toSend);

        context.setMessageToSend(toSend);
        context.setState(new StateAuthentification());
    }
}

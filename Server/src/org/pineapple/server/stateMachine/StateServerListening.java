package org.pineapple.server.stateMachine;

import org.pineapple.CodeOK;
import org.pineapple.CommandPOP3;

public class StateServerListening implements State {

    @Override
    public void handle(Context context, CommandPOP3 command, String[] args) {

        String toSend = CodeOK.CodeEnum.OK_SERVER_READY.toString("Server");

        // TODO : Establish connection ?

        context.setMessageToSend(toSend);
        context.setState(new StateAuthentification());
    }
}

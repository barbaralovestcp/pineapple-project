package org.pineapple.server;

import org.pineapple.CodeOK;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateServerListening implements IState {


    @Override
    public void handle(Context context, IInputStateMachine input) {


        //CommandPOP3 command = ((InputStateMachinePOP3)input).getCommand();
        //String[] args = input.getArguments();
        String toSend =  new CodeOK(CodeOK.CodeEnum.OK_SERVER_READY).toString("Server");

        System.out.println("toSend = " + toSend);

        context.setMessageToSend(toSend);
        context.setState(new StateAuthentification());
    }
}

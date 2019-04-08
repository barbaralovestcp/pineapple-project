package org.pineapple.serversmtp;

import org.pineapple.CodeOKSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateListening implements IState {
    @Override
    public void handle(Context context, IInputStateMachine input) {

        ContextServer contextServer = (ContextServer)context;
        String toSend = new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK).toString(contextServer.getDomain());
        IState nextState = new StateWaitMailFrom();

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

package org.pineapple.serversmtp;

import org.pineapple.CodeOKSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateListening implements IState {
    @Override
    public void handle(Context context, IInputStateMachine input) {

        String toSend = new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK_CONNECTED).toString(((ContextServer)context).getDomain());
        IState nextState = new StateAuthentification();

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

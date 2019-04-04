package org.pineapple.clientsmtp.stateMachine;

import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateConnected implements IState {

    @Override
    public void handle(Context context, IInputStateMachine input) {

        String domain = ((ContextClient) context).getDomain();
        String[] arguments = input.getArguments();
        String toSend = "";
        IState nextState = null;

        if (arguments[0].equals("220")) {
            nextState = new StateWaitingGreeting();
            toSend = CommandSMTP.EHLO + " " + domain;
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }


}

package org.pineapple.clientsmtp.stateMachine;

import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateWaitingGreeting implements IState {
    @Override
    public void handle(Context context, IInputStateMachine input) {

        String domain = ((ContextClient) context).getDomain();
        String name = ((ContextClient) context).getDomain();
        String[] arguments = input.getArguments();
        String toSend = "";
        IState nextState = null;

        if (arguments[0].equals("250")) {
            nextState = new StateWaitingMailFromAnswer();
            toSend = "MAIL FROM:" + " " + name + "@" + domain;
        }
        //TODO : ADD ERROR CASE?
        //invalid answer
        else {
            throw new StateMachineException(this, arguments[0]);
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);

    }
}

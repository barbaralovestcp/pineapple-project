package org.pineapple.clientsmtp.stateMachine;

import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateConnected implements IState {

    @Override
    public void handle(Context context, IInputStateMachine input) {

        String[] arguments = input.getArguments();
        String toSend = "";
        IState nextState = null;

        if (arguments[0].equals("250")) {
            nextState = new StateWaitingGreeting();
            toSend = CommandSMTP.EHLO + " client"; //TODO : Specify Client?
        }
        //TODO : ADD ERROR CASE?
        else {
            throw new StateMachineException(this, arguments[0]);
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }


}

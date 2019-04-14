package org.pineapple.clientsmtp.stateMachine;

import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateWaitingGreeting implements IState {
    @Override
    public void handle(Context context, IInputStateMachine input) {

        String[] arguments = input.getArguments();
        if(arguments[0].equals("550")){
            throw new StateMachineException(this, arguments[0]);
        }
        //else nothing : no mail to send
    }
}

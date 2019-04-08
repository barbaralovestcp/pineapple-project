package org.pineapple.serversmtp;

import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateReceivingData implements IState {
    @Override
    public void handle(Context context, IInputStateMachine input) {
        String[] args = input.getArguments();
        CommandSMTP command = ((InputStateMachineSMTP)input).getCommand();

        String toSend = "";
        IState nextState = null;

        //TODO : Pas de commande, on doit PARSE args !

        //TODO : A COMPLETER

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

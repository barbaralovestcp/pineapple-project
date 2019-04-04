package org.pineapple.clientsmtp.stateMachine;

import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

import java.util.ArrayList;

public class StateWaitingMailFromAnswer implements IState {

    @Override
    public void handle(Context context, IInputStateMachine input) {

        ArrayList<String> recipients = ((ContextClient) context).getRecipient();
        if(recipients.size() == 0){
            throw new StateMachineException(this, "Number of recipients cannot be 0");
        }

        String[] arguments = input.getArguments();
        String toSend = "";
        IState nextState = null;

        //case ok
        if (arguments[0].equals("250")) {

            if (arguments[1].equals("MAIL")) {
                nextState = new StateWaitingRcptAnswer();
                toSend = "RCPT:" + recipients.get(0);
                ((ContextClient) context).iterate();
            }
            else if (arguments[1].equals("QUIT")) {
                nextState = new StateConnected(); //TODO : Bon state?
            }
        }
        //case err
        else if (arguments[0].equals("550")){
            nextState = this;
            toSend = "ERR 550"; //TODO : Build message error
        }
        //invalid answer
        else {
            throw new StateMachineException(this, arguments[0]);
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

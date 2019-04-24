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

        String[] arguments = input.getArguments();
        String toSend = "";
        IState nextState = null;

        //case ok
        if (arguments[0].equals("250")) {

            if (arguments[1].toLowerCase().equals("mail")) {
                if(recipients.size() == 0){
                    throw new StateMachineException(this, "Number of recipients cannot be 0");
                }
                nextState = new StateWaitingRcptAnswer();
                toSend = "RCPT " + recipients.get(0);
                ((ContextClient) context).iterateRecipentSend();
            }
            else if (arguments[1].toLowerCase().equals("quit")) {
                nextState = new StateConnected();
                context.setToQuit(true);
            }else{
                nextState = this;
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

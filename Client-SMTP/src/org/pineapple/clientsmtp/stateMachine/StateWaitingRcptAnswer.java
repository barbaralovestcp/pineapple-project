package org.pineapple.clientsmtp.stateMachine;

import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

import java.util.ArrayList;

public class StateWaitingRcptAnswer implements IState {

    @Override
    public void handle(Context context, IInputStateMachine input) {

        ArrayList<String> recipients = ((ContextClient) context).getRecipient();
        assert (recipients.size() > 0);

        String[] arguments = input.getArguments();
        String toSend = "";
        IState nextState = null;

        //case ok
        if (arguments[0].equals("250")) {

            if (arguments[1].equals("RCPT")) {
                if(((ContextClient) context).getRecipientIterator() < recipients.size()){
                    nextState = new StateWaitingRcptAnswer();
                    toSend = "RCPT:" + recipients.get(((ContextClient) context).getRecipientIterator());
                    ((ContextClient) context).iterate();
                }else{
                    nextState = new StateWaitingDataReady();
                    toSend = "DATA";
                }
            }
        }
        //case err
        else if (arguments[0].equals("550")){
            nextState = new StateWaitingMailFromAnswer();
            toSend = "REST";
        }
        //invalid answer
        else {
            throw new StateMachineException(this, arguments[0]);
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

package org.pineapple.clientsmtp.stateMachine;

import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

import java.util.ArrayList;
import java.util.Arrays;

public class StateWaitingRcptAnswer implements IState {

    @Override
    public void handle(Context context, IInputStateMachine input) {

        ArrayList<String> recipients = ((ContextClient) context).getRecipient();
        assert (recipients.size() > 0);

        String[] arguments = input.getArguments();
        String toSend = "";
        IState nextState = null;

        //case rcpt asnwer
        if (Arrays.toString(input.getArguments()).toLowerCase().contains("user")) {
            if (arguments[0].equals("250")) {
                ((ContextClient) context).iterateRecipentValid();
            }

            //sending next rcpt
            if(((ContextClient) context).getRecipientIterator() < recipients.size()){
                nextState = this;
                toSend = "RCPT " + recipients.get(((ContextClient) context).getRecipientIterator());
                ((ContextClient) context).iterateRecipentSend();
            }else{

                // no valid rcpt : RCT
                if(((ContextClient) context).getValidIRecipient() == 0){
                    nextState = new StateWaitingMailFromAnswer();
                    toSend = "RSET";
                }else{
                    nextState = new StateWaitingDataReady();
                    toSend = "DATA";
                }
            }
        }
        //invalid answer
        else {
            throw new StateMachineException(this, arguments[0]);
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

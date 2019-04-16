package org.pineapple.serversmtp;

import org.pineapple.CodeOKSMTP;
import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

import java.util.Arrays;

public class StateReceivingData implements IState {
    @Override
    public void handle(Context context, IInputStateMachine input) {
        String[] args = input.getArguments();
        //CommandSMTP command = ((InputStateMachineSMTP)input).getCommand();

        ContextServer contextServer = (ContextServer) context;

        String toSend = "";
        IState nextState;

        boolean endOfData = false;
        StringBuilder body = contextServer.getMailBody();

        //Parsing arguments :

        //split by line returns
        String[] lines = args[1].split("\\r\\n");
        for (int j = 0; j < lines.length; j++) {

            //It is the last line ?
            if (lines[j].equals(".")) {
                endOfData = true;
                break;
            } else {
                //Add the line to the body
                body.append(lines[j]).append("\r\n");
            }
        }


        if (endOfData) {
            //Data received, back to waiting the next mail
            toSend = new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK_DATA_RECEIVED).toString();
            nextState = new StateWaitMailFrom();

            //Save the mail in the mailbox
            contextServer.saveCurrentMessage();

            //Clear the mail informations
            contextServer.clearMailData();
        } else {
            //We wait for more data.
            nextState = this;
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

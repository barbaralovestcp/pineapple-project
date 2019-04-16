package org.pineapple.serversmtp;

import org.pineapple.CodeOKSMTP;
import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateReceivingData implements IState {
    @Override
    public void handle(Context context, IInputStateMachine input) {
        String[] args = input.getArguments();

        //CommandSMTP command = ((InputStateMachineSMTP)input).getCommand();

        ContextServer contextServer = (ContextServer)context;

        String toSend = "";
        IState nextState;

        boolean endOfData = false;
        StringBuilder body = contextServer.getMailBody();

        //Parsing arguments :

        //all the body should be in only one of the arguments case! (I dont remember which one, 0 or 1)
        for (int i = 0; i < args.length; i++) {

            //split by line returns
            String[] lines = args[i].split("\\r\\n");
            for (int j = 0; j < lines.length; j++) {

                //It is the last line ?
                if (lines[j].equals(".")) {
                    endOfData = true;
                    break;
                }
                else {
                    //Add the line to the body
                    body.append(lines[j]).append("\r\n");
                }
            }

            if (endOfData) {
                break;
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
        }
        else {
            //We wait for more data.
            nextState = this;
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

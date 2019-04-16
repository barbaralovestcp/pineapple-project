package org.pineapple.serversmtp;

import org.pineapple.CodeDATASMTP;
import org.pineapple.CodeERRSMTP;
import org.pineapple.CodeOKSMTP;
import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateWaitRCPT implements IState {


    @Override
    public void handle(Context context, IInputStateMachine input) {
        String[] args = input.getArguments();
        CommandSMTP command = ((InputStateMachineSMTP)input).getCommand();

        ContextServer contextServer = (ContextServer)context;

        String toSend = "";
        IState nextState = null;

        switch (command) {
            case RCPT:
                String rcpt = args[1];

                //Un mail est valide si il existe une mailbox pour ce mail :
                //TODO : FAIRE VALIDATION
                boolean rcptIsValid = true;

                if (rcptIsValid) {
                    contextServer.addMailRcpt(rcpt);
                    toSend = new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK_USER_FOUND).toString();
                    nextState = this;
                }
                else {
                    toSend = new CodeERRSMTP(CodeERRSMTP.CodeEnum.ERR_INVALID_RCPT).toString();
                    nextState = this;
                }
                break;
            case DATA:

                //TODO : Verifier qu'il y a au moins un destinataire valide (A gérer coté client?)

                toSend = new CodeDATASMTP(CodeDATASMTP.CodeEnum.START).toString();
                nextState = new StateReceivingData();
                break;
            case RSET:
                toSend = new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK).toString();
                nextState = new StateWaitMailFrom();

                //clear mail data
                contextServer.clearMailData();
                break;
            default:
                throw new StateMachineException(this, command);
        }


        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

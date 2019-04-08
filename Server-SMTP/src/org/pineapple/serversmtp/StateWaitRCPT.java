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

        String toSend = "";
        IState nextState = null;

        switch (command) {
            case RCPT:
                //TODO : Traiter RCPT

                //TODO : Un mail est valide si il existe une mailbox pour ce mail
                boolean rcptIsValid = true;
                if (rcptIsValid) {
                    //TODO : Ajouter mail à une liste de destinataire dans ContextServer
                    toSend = new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK_USER_FOUND).toString();
                    nextState = this;
                }
                else {
                    toSend = new CodeERRSMTP(CodeERRSMTP.CodeEnum.ERR_INVALID_RCPT).toString();
                    nextState = this;
                }
                break;
            case DATA:
                //TODO : Verifier qu'il y a au moins un destinataire valide.

                toSend = new CodeDATASMTP(CodeDATASMTP.CodeEnum.START).toString();
                nextState = new StateReceivingData();
                break;
            case REST:
                toSend = new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK).toString();
                nextState = new StateAuthentification();
                break;
            default:
                throw new StateMachineException(this, command);
        }


        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

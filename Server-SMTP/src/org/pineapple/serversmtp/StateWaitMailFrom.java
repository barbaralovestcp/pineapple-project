package org.pineapple.serversmtp;

import org.pineapple.CodeERRSMTP;
import org.pineapple.CodeOKSMTP;
import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.InvalidCommandArgumentsException;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateWaitMailFrom implements IState {

    @Override
    public void handle(Context context, IInputStateMachine input) {

        String[] args = input.getArguments();
        CommandSMTP command = ((InputStateMachineSMTP)input).getCommand();
        ContextServer contextServer = (ContextServer)context;

        String toSend = "";
        IState nextState = null;

        switch (command) {
            case MAIL:
                //TODO : Traiter MAIL

                if (args.length < 2) {
                    throw new InvalidCommandArgumentsException(command, "There must be at least 3 arguments !");
                }

                String mail = args[2];
                System.out.println("DEBUG -- Mail : " + mail);
                boolean mailIsValid = true; //TODO : VERIFIER LE MAIL?
                if (mailIsValid) {

                    contextServer.setMailFrom(mail);
                    toSend = new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK_MAIL_FROM).toString();
                    nextState = new StateWaitRCPT();
                }
                else {
                    toSend = new CodeERRSMTP(CodeERRSMTP.CodeEnum.ERR_MAIL_FROM).toString();
                    nextState = this; //A changer?
                }
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

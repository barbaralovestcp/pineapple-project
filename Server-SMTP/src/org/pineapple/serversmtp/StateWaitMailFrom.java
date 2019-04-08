package org.pineapple.serversmtp;

import org.pineapple.CodeERRSMTP;
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
                boolean mailIsValid = true;
                if (mailIsValid) {
                    contextServer.setMailFrom(mail);
                    //TODO : Traiter mail valide
                    nextState = new StateWaitRCPT();
                }
                else {
                    //TODO : Traiter ERROR
                    toSend = new CodeERRSMTP(CodeERRSMTP.CodeEnum.ERR_MAIL_FROM).toString();
                    nextState = this; //A changer?
                }
                break;
            default:
                throw new StateMachineException(this, command);
        }


        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

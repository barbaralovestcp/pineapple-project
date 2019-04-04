package org.pineapple.serversmtp;

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
                //TODO : Traiter MAIL

                boolean rcptIsValid = true;
                if (rcptIsValid) {
                    //TODO : Traiter mail valide
                    nextState = new StateTransaction();
                }
                else {
                    //TODO : Traiter erreur
                    nextState = new StateWaitRCPT(); //A changer?
                }
                break;
            default:
                throw new StateMachineException(this, command);
        }


        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}
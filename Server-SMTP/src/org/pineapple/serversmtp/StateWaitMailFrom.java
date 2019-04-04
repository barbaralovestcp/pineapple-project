package org.pineapple.serversmtp;

import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateWaitMailFrom implements IState {

    @Override
    public void handle(Context context, IInputStateMachine input) {

        String[] args = input.getArguments();
        CommandSMTP command = ((InputStateMachineSMTP)input).getCommand();

        String toSend = "";
        IState nextState = null;

        switch (command) {
            case MAIL:
                //TODO : Traiter MAIL

                boolean mailIsValid = true;
                if (mailIsValid) {
                    //TODO : Traiter mail valide
                    nextState = new StateWaitRCPT();
                }
                else {
                    //TODO : Traiter ERROR
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

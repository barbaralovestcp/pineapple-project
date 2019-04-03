package org.pineapple.clientsmtp.stateMachine;

import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateWaitingMailFromAnswer implements IState {

    @Override
    public void handle(Context context, IInputStateMachine input) {

        String[] arguments = input.getArguments();
        String toSend = "";
        IState nextState = null;

        //case ok
        if (arguments[0].equals("250")) {

            if (arguments[2].equals("mail")) {
                nextState = null; //TODO : Replace with next state when class is created
                toSend = "RCPT:" + "<Get mail>"; //TODO : Construit la bonne commande
            }
            else if (arguments[2].equals("QUIT")) {
                nextState = new StateConnected(); //TODO : Bon state?
                toSend = "SHUTDOWN"; //TODO : Ecrire la bonne réponse
            }
        }
        //case err
        else if (arguments[0].equals("550")){
            nextState = this;
            toSend = "ERR 550"; //TODO : Build message error
        }
        //invalid answer
        else {
            throw new StateMachineException(this, arguments[0]);
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

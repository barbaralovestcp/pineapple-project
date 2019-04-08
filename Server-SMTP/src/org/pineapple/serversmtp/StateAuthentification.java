package org.pineapple.serversmtp;


import org.pineapple.CodeOKSMTP;
import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateAuthentification implements IState {

    @Override
    public void handle(Context context, IInputStateMachine input) {

        ContextServer contextServer = (ContextServer)context;
        String[] args = input.getArguments();
        CommandSMTP command = ((InputStateMachineSMTP)input).getCommand();

        String toSend = "";
        IState nextState = null;

        switch (command) {
            case EHLO:
                //TODO : Traiter EHLO
                toSend = new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK_CONNECTED).toString(contextServer.getDomain());
                nextState = new StateWaitMailFrom();
                break;
            case QUIT:
                //TODO : Traiter QUIT
                toSend = new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK_QUIT).toString();
                nextState = new StateListening();
                contextServer.setToQuit(true);

                break;
            default:
                throw new StateMachineException(this, command);
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }

    
}

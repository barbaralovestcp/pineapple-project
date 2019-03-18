package org.pineapple.server.stateMachine;

import org.pineapple.CommandPOP3;
import org.pineapple.server.stateMachine.Exception.StateMachineException;

public class StateAuthentification implements State {


    @Override
    public void handle(Context context, CommandPOP3 command, String[] args) {

        String toSend;
        State nextState = null;
        switch (command) {
            case APOP:

                //TODO : Verify if APOP is Valid
                boolean popIsValid = true;
                if (popIsValid)
                {
                    toSend = "OK maildrop locked and ready";
                    nextState = new StateTransaction();
                }
                else {
                    toSend = "ERR Permission denied";
                    nextState = this;
                }

                break;
            case QUIT:

                toSend = "OK";

                //TODO : End connection ?
                nextState = new StateServerListening();

                break;
            default:
                throw new StateMachineException(this, command);
        }


        //TODO : SEND MESSAGE
        System.out.println(toSend);
        context.setState(nextState);

    }
}

package org.pineapple.server.stateMachine;

import org.pineapple.server.stateMachine.Exception.StateMachineException;

public class StateTransaction implements State {


    @Override
    public void handle(Context context, CommandPOP3 command, String[] args) {

        String toSend = "";
        State nextState = null;
        switch (command) {

            case STAT:

                //TODO : Fill message with STATS
                toSend = "<Stats !>";
                nextState = this;
                break;
            case RETR:

                //TODO : Retrieve mails
                toSend = "<Retrieved Mail>";
                nextState = this;
                break;
            case QUIT:

                boolean quitIsValid = true;
                if (quitIsValid) {
                    toSend = "OK";
                }
                else {
                    toSend = "ERR some deleted message not removed";
                }

                //TODO : End connection (?)

                nextState = new StateServerListening();
                break;
            default :
                throw new StateMachineException(this, command);
        }

        //TODO : Send message;
        System.out.println(toSend);
        context.setState(nextState);
    }
}

package org.pineapple.server.stateMachine;

import org.pineapple.CodeERR;
import org.pineapple.CodeOK;
import org.pineapple.CommandPOP3;
import org.pineapple.server.stateMachine.Exception.StateMachineException;

public class StateTransaction implements State {


    @Override
    public void handle(Context context, CommandPOP3 command, String[] args) {

        String toSend = "";
        State nextState = null;
        switch (command) {

            case STAT:

                //TODO: Fill message with STATS
                toSend = CodeOK.CodeEnum.OK_STAT.toString("<Stats !>");
                nextState = this;
                break;
            case RETR:

                //TODO : Retrieve mails
                toSend = CodeOK.CodeEnum.OK_RETRIEVE.toString("<Retrieved Mail>");
                nextState = this;
                break;
            case QUIT:

                boolean quitIsValid = true;
                if (quitIsValid) {
                    toSend = CodeOK.CodeEnum.OK.toString();
                }
                else {
                    toSend = CodeERR.CodeEnum.ERR_DEL_MESSAGE_NOT_REMOVED.toString();
                }
                context.setToQuit(true);

                //TODO : End connection (?)

                nextState = new StateServerListening();
                break;
            default :
                throw new StateMachineException(this, command);
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

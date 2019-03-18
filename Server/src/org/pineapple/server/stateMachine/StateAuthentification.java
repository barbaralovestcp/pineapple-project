package org.pineapple.server.stateMachine;

import org.pineapple.CodeERR;
import org.pineapple.CodeOK;
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
                    toSend = CodeOK.CodeEnum.OK_MAILDROP_READY.toString();
                    nextState = new StateTransaction();
                }
                else {
                    toSend = CodeERR.CodeEnum.ERR_PERMISSION_DENIED.toString();
                    nextState = this;
                }

                break;
            case QUIT:
                
                toSend = CodeOK.CodeEnum.OK.toString();
                context.setToQuit(true);
                
                //TODO : End connection ?
                nextState = new StateServerListening();
                
                break;
            default:
                throw new StateMachineException(this, command);
        }
        
        context.setMessageToSend(toSend);
        context.setState(nextState);

    }
}

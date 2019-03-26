package org.pineapple.server.stateMachine;

import org.pineapple.*;
import org.pineapple.server.stateMachine.Exception.StateMachineException;

public class StateAuthentification implements State {


    @Override
    public void handle(Context context, CommandPOP3 command, String[] args) {

        String toSend;
        State nextState = null;
        switch (command) {
            case APOP:

                if (args.length < 3 ) {
                    System.out.println("Missing APOP arguments !");
                    throw new StateMachineException(this, command);
                }
                
                if (UserManager.checkPassword(args[1], args[2], false)) {
                    MailBox mailbox = new MailBox(args[1], args[2]);
                    /*mailbox.addMessages(Message.getTestMessage());
                    mailbox.addMessages(Message.getTestMessage());*/
                    System.out.println("USER : " + mailbox.getName() + " PASS : " + mailbox.getPassword());
                    context.setMailBox(mailbox);
                    toSend = new CodeOK(CodeOK.CodeEnum.OK_MAILDROP_READY).toString();
                    nextState = new StateTransaction();
                }
                else {
                    toSend = new CodeERR(CodeERR.CodeEnum.ERR_PERMISSION_DENIED).toString();
                    nextState = this;
                }

                break;
            case QUIT:
                
                toSend = new CodeOK(CodeOK.CodeEnum.OK).toString();
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

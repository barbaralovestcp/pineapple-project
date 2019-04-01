package org.pineapple.server;

import org.pineapple.*;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.Exception.InvalidPOP3ArgumentsException;
import org.pineapple.stateMachine.Exception.StateMachineException;
import org.pineapple.stateMachine.State;

public class StateAuthentification implements State {


    @Override
    public void handle(Context context, CommandPOP3 command, String[] args) {

        String toSend;
        State nextState = null;
        switch (command) {
            case APOP:

                if (args.length < 3 ) {
                    //System.out.println("Missing APOP arguments ! (Username, password)");
                    throw new InvalidPOP3ArgumentsException(command, "Missing arguments! Arguments expected : Username, password");
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

                nextState = new StateServerListening();
                
                break;
            default:
                throw new StateMachineException(this, command);
        }
        
        context.setMessageToSend(toSend);
        context.setState(nextState);

    }
}

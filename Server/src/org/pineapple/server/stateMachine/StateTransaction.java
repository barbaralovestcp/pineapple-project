package org.pineapple.server.stateMachine;

import org.pineapple.*;
import org.pineapple.server.stateMachine.Exception.StateMachineException;

import java.util.ArrayList;

public class StateTransaction implements State {


    @Override
    public void handle(Context context, CommandPOP3 command, String[] args) {

        String toSend = "";
        State nextState = null;
        MailBox mailbox = context.getMailBox();
        switch (command) {

            case STAT:

                toSend = CodeOK.CodeEnum.OK_STAT.toString("+OK " + mailbox.getMessagesNumber() + " " + mailbox.getTotalSize());
                nextState = this;
                break;
            case RETR:


                //TODO : Add Error missing arguments
                if (args.length < 2) {
                    System.out.println("Missing RETR arguments !");
                    throw new StateMachineException(this, command);
                }

                int indexMessage = 0;
                try {
                    indexMessage = Integer.parseInt(args[1]);
                } catch (NumberFormatException err) {
                    System.out.println("RETR argument is not a number !");
                    throw new StateMachineException(this, command);
                }

                //TODO : Retrieve mails
                //prepare string
                ArrayList<Message> messages = mailbox.getMessages();

                if (indexMessage > messages.size() || indexMessage <= 0) {
                    System.out.println("RETR : There's no " + indexMessage + "th message !");
                    throw new StateMachineException(this, command);
                }

                //Get all messages
                /*for (int i = 0; i < messages.size(); i++) {
                    toSend += messages.get(i).buildMessage() + "\n";
                }
                toSend = CodeOK.CodeEnum.OK_RETRIEVE.toString(toSend);*/

                toSend = CodeOK.CodeEnum.OK_RETRIEVE.toString(messages.get(indexMessage - 1).buildMessage());

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

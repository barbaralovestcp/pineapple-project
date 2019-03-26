package org.pineapple.server.stateMachine;

import org.pineapple.*;
import org.pineapple.server.stateMachine.Exception.InvalidPOP3ArgumentsException;
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

                toSend = new CodeOK(CodeOK.CodeEnum.OK_STAT).toString(mailbox.getMessagesNumber() + " " + mailbox.getTotalSize());
                nextState = this;
                break;
            case RETR:

                if (args.length < 2) {
                    //System.out.println("Missing RETR arguments !");
                    throw new InvalidPOP3ArgumentsException(command, "Missing arguments! Arguments expected : Message number");
                }

                int indexMessage = 0;
                try {
                    indexMessage = Integer.parseInt(args[1]);
                } catch (NumberFormatException err) {
                    //System.out.println("RETR argument is not a valid number !");
                    throw new InvalidPOP3ArgumentsException(command, "Invalid arguments! Argument is not a valid number.");
                }

                //prepare string
                ArrayList<Message> messages = mailbox.getMessages();

                if (indexMessage > messages.size() || indexMessage <= 0) {
                    //System.out.println("RETR : There's no " + indexMessage + "th message !");
                    throw new InvalidPOP3ArgumentsException(command, "Invalid arguments! There's no " + indexMessage + "th message.");
                }

                //Get all messages
                /*for (int i = 0; i < messages.size(); i++) {
                    toSend += messages.get(i).buildMessage() + "\n";
                }
                toSend = CodeOK.CodeEnum.OK_RETRIEVE.toString(toSend);*/

                toSend = new CodeOK(CodeOK.CodeEnum.OK_RETRIEVE).toString(messages.get(indexMessage - 1).buildMessage());

                nextState = this;
                break;
            case QUIT:

                boolean quitIsValid = true;
                if (quitIsValid) {
                    toSend = new CodeOK(CodeOK.CodeEnum.OK).toString();
                }
                else {
                    toSend = new CodeERR(CodeERR.CodeEnum.ERR_DEL_MESSAGE_NOT_REMOVED).toString();
                }
                context.setToQuit(true);

                nextState = new StateServerListening();
                break;
            default :
                throw new StateMachineException(this, command);
        }

        context.setMessageToSend(toSend);
        context.setState(nextState);
    }
}

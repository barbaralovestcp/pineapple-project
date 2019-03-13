package org.pineapple.server.stateMachine;

public class StateTransaction implements State {

    @Override
    public void onStateEntry(Context context) { }

    @Override
    public void handle(Context context, CommandPOP3 entry, String[] args) {

        String toSend = "";
        switch (entry) {

            case STAT:

                //TODO : Fill message with STATS
                break;
            case RETR:

                //TODO : Retrieve mails
                break;
            case QUIT:

                boolean quitIsValid = true;
                if (quitIsValid) {
                    toSend = "ERR some deleted message not removed";
                }
                else {
                    toSend = "OK";
                }

                //TODO : End connection (?)

                context.setState(new StateServerListening());
                break;
            default :
                throw new StateMachineException("Unhandled POP3 entry for this state.");
        }

        //TODO : Send message;
        System.out.println(toSend);
    }
}

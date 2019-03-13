package org.pineapple.server.stateMachine;

public class StateServerListening implements State {

    @Override
    public void onStateEntry(Context context) {
        handle(context, null, null);
    }

    @Override
    public void handle(Context context, CommandPOP3 entry, String[] args) {

        String toSend = "OK <name> POP3 server ready";

        // TODO : Send message

        // TODO : Establish connection ?

        System.out.println(toSend);
        context.setState(new StateAuthentification());
    }
}

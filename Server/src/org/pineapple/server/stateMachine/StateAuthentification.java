package org.pineapple.server.stateMachine;

public class StateAuthentification implements State {

    @Override
    public void onStateEntry(Context context) { }

    @Override
    public void handle(Context context, CommandPOP3 entry, String[] args) {

        String toSend;
        switch (entry) {
            case APOP:

                //TODO : Verify if APOP is Valid
                boolean popIsValid = true;
                if (popIsValid)
                {
                    toSend = "ERR Permission denied";
                    //No change of state here.
                }
                else {
                    toSend = "OK maildrop locked and ready";
                    context.setState(new StateTransaction());

                }

                break;
            case QUIT:

                toSend = "OK";

                //TODO : End connection ?

                break;
            default:
                throw new StateMachineException("Unhandled POP3 entry for this state.");
        }


        //TODO : SEND MESSAGE
        System.out.println(toSend);
    }
}

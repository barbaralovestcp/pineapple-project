package org.pineapple.server.stateMachine;

public interface State {

    /**
     * This method is executed when this state is entered.
     * @param context
     */
    void onStateEntry(Context context);

    /**
     * Process the input parameters, then moves to the next state.
     * @param context Reference to the state machine
     * @param entry Pop3 command
     * @param args optional arguments for the Pop3 command
     */
    void handle(final Context context, final CommandPOP3 entry, final String[] args);

}

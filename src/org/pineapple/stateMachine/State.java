package org.pineapple.stateMachine;

import org.pineapple.CommandPOP3;

public interface State {

    /**
     * Process the input parameters, then moves to the next state.
     * @param context Reference to the state machine
     * @param command Pop3 command
     * @param args optional arguments for the Pop3 command
     */
    void handle(final Context context, final CommandPOP3 command, final String[] args);

}

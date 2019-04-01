package org.pineapple.serversmtp;

import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateListening implements IState {
    @Override
    public void handle(Context context, IInputStateMachine input) {

        /**
         * Pas de traitement, renvoyer OK ? (Comme ServerListening POP3 ?)
         */
    }
}

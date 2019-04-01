package org.pineapple.serversmtp;

import org.pineapple.CommandSMTP;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

public class StateTransaction implements IState {
    @Override
    public void handle(Context context, IInputStateMachine input) {

        String[] args = input.getArguments();
        CommandSMTP command = ((InputStateMachineSMTP)input).getCommand();
    }
}

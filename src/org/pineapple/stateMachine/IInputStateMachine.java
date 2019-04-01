package org.pineapple.stateMachine;

import org.pineapple.ICommand;

public interface IInputStateMachine {

    String[] getArguments();

    ICommand getCommand();
}

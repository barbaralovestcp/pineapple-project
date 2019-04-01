package org.pineapple.stateMachine;


public interface IState {

    void handle(final Context context, final IInputStateMachine input);

}

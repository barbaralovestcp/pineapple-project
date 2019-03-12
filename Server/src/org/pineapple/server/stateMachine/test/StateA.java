package org.pineapple.server.stateMachine.test;

import org.pineapple.server.stateMachine.Command;
import org.pineapple.server.stateMachine.Context;
import org.pineapple.server.stateMachine.State;

public class StateA implements State {

    @Override
    public void handle(Context context, Command entry, String arg) {

        System.out.println("I'm State A !");

        context.setState(new StateB());
    }
}

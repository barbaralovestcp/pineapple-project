package org.pineapple.server.stateMachine;

public interface State {

    void handle(Command entry, String arg );

}

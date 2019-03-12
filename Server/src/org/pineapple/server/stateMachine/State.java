package org.pineapple.server.stateMachine;

public interface State {

    void handle(final Context context, final Command entry, final String arg);

}

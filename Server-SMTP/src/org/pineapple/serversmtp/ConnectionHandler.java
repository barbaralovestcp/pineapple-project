package org.pineapple.serversmtp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pineapple.AbstractServerConnectionHandler;
import org.pineapple.CommandSMTP;
import org.pineapple.ICommand;
import org.pineapple.stateMachine.AbstractInputStateMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConnectionHandler extends AbstractServerConnectionHandler<CommandSMTP> {
	
	public ConnectionHandler(@NotNull Socket so_client, @Nullable Consumer<String> onLog) throws IOException {
		super(so_client, onLog, new StateListening(), InputStateMachineSMTP.class, InputStateMachineSMTP::new);
	}
	public ConnectionHandler(@NotNull Socket so_client) throws IOException {
		this(so_client, null);
	}
}

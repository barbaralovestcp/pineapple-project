package org.pineapple.serversmtp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pineapple.AbstractServerConnectionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Function;

public class ConnectionHandler extends AbstractServerConnectionHandler<InputStateMachineSMTP> {
	
	public ConnectionHandler(@NotNull Socket so_client, @Nullable Function<String, Void> onLog) throws IOException {
		super(so_client, onLog, new StateListening(), InputStateMachineSMTP.class, InputStateMachineSMTP::new);
	}
	public ConnectionHandler(@NotNull Socket so_client) throws IOException {
		this(so_client, null);
	}
}

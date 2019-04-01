package org.pineapple.server;

import org.jetbrains.annotations.NotNull;
import org.pineapple.AbstractServer;
import org.pineapple.AbstractServerConnectionHandler;

import java.io.IOException;
import java.net.Socket;

public class Server extends AbstractServer implements Runnable {
	
	public Server() {
		super(110);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	protected @NotNull AbstractServerConnectionHandler instantiateConnectionHandler(@NotNull Socket com_cli) throws IOException {
		return new ConnectionHandler(com_cli, s -> {
			log(s);
			return null;
		});
	}
}
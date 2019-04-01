package org.pineapple.serversmtp;

import org.jetbrains.annotations.NotNull;
import org.pineapple.AbstractServer;
import org.pineapple.AbstractServerConnectionHandler;

import java.io.IOException;
import java.net.Socket;

public class ServerSMTP extends AbstractServer implements Runnable {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	protected @NotNull AbstractServerConnectionHandler instanciateConnectionHandler(@NotNull Socket com_cli) throws IOException {
		/*return new ConnectionHandler(com_cli, s -> {
			log(s);
			return null;
		});*/
		return null;
	}
}
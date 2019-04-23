package org.pineapple.serversmtp;

import org.jetbrains.annotations.NotNull;
import org.pineapple.AbstractServer;
import org.pineapple.AbstractServerConnectionHandler;
import org.pineapple.MBXManager;

import java.io.IOException;
import java.net.Socket;

public class ServerSMTP extends AbstractServer implements Runnable {
	
	public ServerSMTP() {
		super(1025);
		// Disable MD5/AES file encryption for the SMTP server, in order to read the mail as text files.
		MBXManager.setUseEncryption(false);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	protected @NotNull AbstractServerConnectionHandler instantiateConnectionHandler(@NotNull Socket com_cli) throws IOException {
		return new ConnectionHandler(com_cli, this::log);
	}
}
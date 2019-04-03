package org.pineapple.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pineapple.AbstractServerConnectionHandler;
import org.pineapple.CommandPOP3;
import org.pineapple.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Class to manage a connection for the server.
 * This class has been made with the help of Stack Overflow community
 * Source: https://stackoverflow.com/questions/28629669/java-tcp-simple-webserver-problems-with-response-codes-homework?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
 */
public class ConnectionHandler extends AbstractServerConnectionHandler<CommandPOP3> {
	
	private String domain = "pine.apple";
	
	public ConnectionHandler(@NotNull Socket so_client, @Nullable Consumer<String> onLog) throws IOException {
		super(so_client, onLog, new StateServerListening(), InputStateMachinePOP3.class, InputStateMachinePOP3::new);
	}
	public ConnectionHandler(@NotNull Socket so_client) throws IOException {
		this(so_client, null);
	}
}

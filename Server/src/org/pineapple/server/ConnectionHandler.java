package org.pineapple.server;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pineapple.AbstractServerConnectionHandler;
import org.pineapple.Message;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.InputStateMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

/**
 * Class to manage a connection for the server.
 * This class has been made with the help of Stack Overflow community
 * Source: https://stackoverflow.com/questions/28629669/java-tcp-simple-webserver-problems-with-response-codes-homework?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
 */
public class ConnectionHandler extends AbstractServerConnectionHandler implements Runnable {

	private String messToLog;
	@NotNull
	private Context context;

	public ConnectionHandler(@NotNull Socket so_client, @Nullable Function<String, Void> onLog) throws IOException {
		super(so_client, onLog);
		context = new Context(new StateServerListening());
	}
	public ConnectionHandler(@NotNull Socket so_client) throws IOException {
		this(so_client, null);
	}

	@Override
	public void run() {
	    // Transitioning from state "Listening" to "Authentication"
		if (in_data == null)
			throw new NullPointerException();
	  
		// Reading message from client
		BufferedReader br = new BufferedReader(in_data);

		context.handle(null, null);
		this.messToLog = context.getStateToLog();
		tryLog(messToLog);
		String messageToSend = context.popMessageToSend();
		//If there's a message to send, send it
		if (messageToSend != null && !messageToSend.equals("")) {
			tryLog("Sending message \"" + messageToSend.replace("\n", "\n\t") + "\"");
			sendMessage(messageToSend);
		}

		while ( !context.isToQuit() ) {
			StringBuilder content = new StringBuilder();
			try {
				String line;

				//TODO : Sanitize the first ever readLine input (full of giberrish)
				line = br.readLine(); //Read single line, looping block the line
				String[] parts = line.split("[\u0003\u0001]");
				line = parts[parts.length-1];
				System.out.println("Received client request : " + line);
				content.append(line);

				if (content.length() > 0) {

					//Check if client message is an existing command
					if (InputStateMachine.IsValidPOP3Request(content.toString())) {
						InputStateMachine input = new InputStateMachine(content.toString());
						tryLog("Received command: " + input.getCommand());
						context.handle(new InputStateMachine(content.toString()));
						this.messToLog = context.getStateToLog();
						tryLog(messToLog);

						messageToSend = context.popMessageToSend();
						//If there's a message to send, send it
						if (messageToSend != null && !messageToSend.equals("")) {
							tryLog("Sending message \"" + messageToSend.replace("\n", "\n\t") + "\"");
							sendMessage(messageToSend);
						}
						//Quit if to quit.
						else if (context.isToQuit()) {
							tryLog("Connection closed with " + getClientName());
							so_client.close();
						}
					}
					else {
						System.out.println("Invalid POP3 Request !");
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}


	}

	/* CONNECTION HANDLER METHODS */

	//Send a message of the mailbox
	public void sendMailBoxMessage( @NotNull String command, @NotNull String message){
		SimpleDateFormat sdf = new SimpleDateFormat("dd'/'MM'/'yyyy 'at' HH:mm:ss");
		if(out_data != null) {
			out_data.print(command + "\r\n");
			out_data.print(new Message()
						.setSender("POP3 Server")
						.setReceiver(this.getClientName())
						.setDate(sdf.format(new Date()))
						.setMessageId(Long.toString(new Date().getTime()))
						.buildMessage());
			out_data.flush();
			tryLog("Message sent");
		}
	}

	/* GETTERS & SETTERS */
    
    @NotNull
	@Contract(pure = true)
	public Socket getClient() {
		return so_client;
	}

	public void setClient(@NotNull Socket so_client) {
		this.so_client = so_client;
	}

	@Nullable
	@Contract(pure = true)
	public Function<String, Void> getOnLog() {
		return onLog;
	}

	public void setOnLog(@Nullable Function<String, Void> onLog) {
		this.onLog = onLog;
	}

	public void tryLog(String message) {
		if (getOnLog() != null)
			getOnLog().apply(message);
	}
}

package org.pineapple.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pineapple.AbstractServerConnectionHandler;
import org.pineapple.Message;

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
public class ConnectionHandler extends AbstractServerConnectionHandler {

	public ConnectionHandler(@NotNull Socket so_client, @Nullable Function<String, Void> onLog) throws IOException {
		super(so_client, onLog, new StateServerListening());
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
		
		handleStateAndSendMessage();

		// TODO: Generify:
		while (!context.isToQuit()) {
			StringBuilder content = new StringBuilder();
			try {
				String line;

				line = br.readLine(); //Read single line, looping block the line
				String[] parts = line.split("[\u0003\u0001]");
				line = parts[parts.length-1];
				System.out.println("Received client request : " + line);
				content.append(line);

				if (content.length() > 0) {

					//Check if client message is an existing command
					if (InputStateMachinePOP3.isValidRequest(content.toString())) {
						InputStateMachinePOP3 input = new InputStateMachinePOP3(content.toString());
						tryLog("Received command: " + input.getCommand());
						context.handle(new InputStateMachinePOP3(content.toString()));
						this.messToLog = context.getStateToLog();
						tryLog(messToLog);

						String messageToSend = context.popMessageToSend();
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
}

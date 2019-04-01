package org.pineapple.serversmtp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pineapple.AbstractServerConnectionHandler;
import org.pineapple.stateMachine.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Function;

public class ConnectionHandler extends AbstractServerConnectionHandler {
	
	public ConnectionHandler(@NotNull Socket so_client, @Nullable Function<String, Void> onLog) throws IOException {
		super(so_client, onLog, new StateListening());
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
					if (InputStateMachineSMTP.IsValidSMTPRequest(content.toString())) {
						InputStateMachineSMTP input = new InputStateMachineSMTP(content.toString());
						tryLog("Received command: " + input.getCommand());
						context.handle(new InputStateMachineSMTP(content.toString()));
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
						System.out.println("Invalid SMTP Request !");
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}

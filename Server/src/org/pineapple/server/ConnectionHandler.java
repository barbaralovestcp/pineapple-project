package org.pineapple.server;

import jdk.internal.util.xml.impl.Input;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pineapple.CommandPOP3;
import org.pineapple.Message;
import org.pineapple.server.stateMachine.Context;
import org.pineapple.server.stateMachine.InputStateMachine;
import org.pineapple.CodeOK;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

/**
 * Class to manage a connection for the server.
 * This class has been made with the help of Stack Overflow community
 * Source: https://stackoverflow.com/questions/28629669/java-tcp-simple-webserver-problems-with-response-codes-homework?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
 */
public class ConnectionHandler implements Runnable {

    @NotNull
	private Socket so_client;
	private InputStreamReader in_data;
	private PrintStream out_data;
	@NotNull
	private Context context;

	@Nullable
	private Function<String, Void> onLog;

	public ConnectionHandler(@NotNull Socket so_client, @Nullable Function<String, Void> onLog) throws IOException {
		setClient(so_client);

		in_data = new InputStreamReader(so_client.getInputStream());
		out_data = new PrintStream(so_client.getOutputStream());
		context = new Context();
		setOnLog(onLog);

		tryLog("New connection: " + getClientName());
	}
	public ConnectionHandler(@NotNull Socket so_client) throws IOException {
		this(so_client, null);
	}

	@Override
	public void run() {
	    // Transitioning from state "Listening" to "Authentication"
	    context.handle(null, null);
	    
		if (in_data == null)
			throw new NullPointerException();
	  
		// Reading message from client
		BufferedReader br = new BufferedReader(in_data);

		while ( !context.isToQuit() ) {
			StringBuilder content = new StringBuilder();
			try {
				String line;

				//TODO : Sanitize the first ever readLine input (full of giberrish)
				line = br.readLine(); //Read single line, looping block the line
				System.out.println("Received client request : " + line);
				content.append(line);

				if (content.length() > 0) {

					//Check if client message is an existing command
					if (InputStateMachine.IsValidPOP3Request(content.toString())) {
						InputStateMachine input = new InputStateMachine(content.toString());
						tryLog("Received command: " + input.getCommand());
						context.handle(new InputStateMachine(content.toString()));

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


		System.out.println("DEBUG : THREAD IS ENDING");
	}

	public String getClientName() {
		return so_client.getInetAddress().getHostAddress() + ":" + so_client.getPort() + " (\"" + so_client.getInetAddress().getCanonicalHostName() + "\")";
	}

	/* CONNECTION HANDLER METHODS */

	public void sendMessage(@NotNull String message){
		SimpleDateFormat sdf = new SimpleDateFormat("dd'/'MM'/'yyyy 'at' HH:mm:ss");
		if(out_data != null) {
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

 	@Nullable
	public byte[] getFileData(@NotNull File file) {
		FileInputStream in = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] raw = new byte[4096];
		int size;

		try {
			in = new FileInputStream(file);

			while ((size = in.read(raw)) >= 0)
				baos.write(raw, 0, size);

			baos.flush();
			baos.close();

			return baos.toByteArray();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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

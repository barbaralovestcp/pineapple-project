package org.pineapple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pineapple.stateMachine.AbstractInputStateMachine;
import org.pineapple.stateMachine.Context;
import org.pineapple.stateMachine.IInputStateMachine;
import org.pineapple.stateMachine.IState;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * This class is used by ConnectionHandler (POP3) and ConnectionHandler (SMTP). Do not change it just for ConnectionHandler (SMTP)!
 */
public abstract class AbstractServerConnectionHandler<T extends ICommand> implements Runnable {
	
	@NotNull
	protected Socket so_client;
	protected InputStreamReader in_data;
	protected PrintStream out_data;
	
	@Nullable
	protected Consumer<String> onLog;
	
	protected String messToLog;
	@NotNull
	protected Context context;
	
	@NotNull
	protected Class<? extends AbstractInputStateMachine<T>> clazzInputStateMachine;
	@NotNull
	protected Function<String, ? extends AbstractInputStateMachine<T>> inputStateMachineGenerator;
	
	public AbstractServerConnectionHandler(@NotNull Socket so_client, @Nullable Consumer<String> onLog, @NotNull IState initialState, @NotNull Class<? extends AbstractInputStateMachine<T>> clazzInputStateMachine, @NotNull Function<String, ? extends AbstractInputStateMachine<T>> inputStateMachineGenerator) throws IOException {
		setClient(so_client);
		
		in_data = new InputStreamReader(so_client.getInputStream(), StandardCharsets.ISO_8859_1);
		out_data = new PrintStream(so_client.getOutputStream());
		context = new Context(initialState);
		this.clazzInputStateMachine = clazzInputStateMachine;
		this.inputStateMachineGenerator = inputStateMachineGenerator;
		setOnLog(onLog);
		
		tryLog("New connection: " + getClientName());
	}
	public AbstractServerConnectionHandler(@NotNull Socket so_client, @NotNull IState initialState, @NotNull Class<AbstractInputStateMachine<T>> clazzInputStateMachine, @NotNull Function<String, AbstractInputStateMachine<T>> inputStateMachineGenerator) throws IOException {
		this(so_client, null, initialState, clazzInputStateMachine, inputStateMachineGenerator);
	}
	
	public String getClientName() {
		return so_client.getInetAddress().getHostAddress() + ":" + so_client.getPort() + " (\"" + so_client.getInetAddress().getCanonicalHostName() + "\")";
	}
	
	/* CONNECTION HANDLER METHODS */
	
	/**
	 * Send the string `message` to the client.
	 * @param message Message to send
	 */
	public void sendMessage(@NotNull String message){
		if(out_data != null) {
			out_data.print(message.replaceAll("\\r(?!\\n)", "\r\n"));
			System.out.println(String.format("%040x", new BigInteger(1, message.getBytes(StandardCharsets.ISO_8859_1))));
			out_data.flush();
			tryLog("Command sent");
		}
	}
	
	/**
	 * Check if a message is waiting to be sent. If so, send it to the client, otherwise do nothing.
	 * @return The message that have been sent. It is `null` if no message have been sent.
	 */
	@Nullable
	public String sendMessageIfNeeded() {
		String messageToSend = context.popMessageToSend();
		//If there's a message to send, send it
		if (messageToSend != null && !messageToSend.equals("")) {
			tryLog("Sending message \"" + messageToSend.replace("\n", "\n\t") + "\"");
			sendMessage(messageToSend);
		}
		return messageToSend;
	}
	
	/**
	 * Call `context.handle()`, stack the new message to send (if there is) and log the message.
	 */
	public void handleState() {
		context.handle();
		this.messToLog = context.getStateToLog();
		tryLog(messToLog);
	}
	
	@Override
	public void run() {
		// Transitioning from state "Listening" to "Authentication"
		if (in_data == null)
			throw new NullPointerException();
		
		// Reading message from client
		BufferedReader br = new BufferedReader(in_data);
		
		context.handle();
		this.messToLog = context.getStateToLog();
		tryLog(messToLog);
		String messageToSend = context.popMessageToSend();
		//If there's a message to send, send it
		if (messageToSend != null && !messageToSend.equals("")) {
			tryLog("Sending message \"" + messageToSend.replace("\n", "\n\t") + "\"");
			sendMessage(messageToSend);
		}
		
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
					
					Method isValidRequest = clazzInputStateMachine.getMethod("isValidRequest", String.class);
					
					//Check if client message is an existing command
					if ((boolean) isValidRequest.invoke(null, content.toString())) {
						AbstractInputStateMachine input = this.inputStateMachineGenerator.apply(content.toString());
						tryLog("Received command: " + input.getCommand());
						context.handle(this.inputStateMachineGenerator.apply(content.toString()));
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
						System.out.println("Invalid " + clazzInputStateMachine.getSimpleName().replace("InputStateMachine", "") + " Request !");
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
				System.exit(-1);
			}
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
	public Consumer<String> getOnLog() {
		return onLog;
	}
	
	public void setOnLog(@Nullable Consumer<String> onLog) {
		this.onLog = onLog;
	}
	
	public void tryLog(@Nullable String message) {
		if (getOnLog() != null)
			getOnLog().accept(message);
	}
}

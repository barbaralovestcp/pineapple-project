package org.pineapple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

public abstract class AbstractServerConnectionHandler implements Runnable {
	
	@NotNull
	protected Socket so_client;
	protected InputStreamReader in_data;
	protected PrintStream out_data;
	
	@Nullable
	protected Function<String, Void> onLog;
	
	public AbstractServerConnectionHandler(@NotNull Socket so_client, @Nullable Function<String, Void> onLog) throws IOException {
		setClient(so_client);
		
		in_data = new InputStreamReader(so_client.getInputStream(), StandardCharsets.ISO_8859_1);
		out_data = new PrintStream(so_client.getOutputStream());
		setOnLog(onLog);
		
		tryLog("New connection: " + getClientName());
	}
	public AbstractServerConnectionHandler(@NotNull Socket so_client) throws IOException {
		this(so_client, null);
	}
	
	public String getClientName() {
		return so_client.getInetAddress().getHostAddress() + ":" + so_client.getPort() + " (\"" + so_client.getInetAddress().getCanonicalHostName() + "\")";
	}
	
	/* CONNECTION HANDLER METHODS */
	
	public void sendMessage(@NotNull String message){
		if(out_data != null) {
			out_data.print(message.replaceAll("\\r(?!\\n)", "\r\n"));
			System.out.println(String.format("%040x", new BigInteger(1, message.getBytes(StandardCharsets.ISO_8859_1))));
			out_data.flush();
			tryLog("Command sent");
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

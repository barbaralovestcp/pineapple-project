package org.pineapple.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client{

	static byte data [];
	static String path;
	static boolean quitter = false;

	private String adress = "127.0.0.1";
	private boolean connected = false;
	private String name;
	private OutputStream op;

	public Client(String name) {
		System.out.println("Connexion...");
		try{
			Socket con_serv = new Socket(InetAddress.getByName(adress),110);
			try {
				//flux d'entrée
				InputStream inp = con_serv.getInputStream();
				//flux de sortie
				this.op = con_serv.getOutputStream();

				DataInputStream indata = new DataInputStream(inp);

				StringBuilder answer = new StringBuilder();
				String line = indata.readLine();
				if(line != null){
					System.out.println("Message " + line);

					if(line.contains("+OK")){
						this.setConnected(true);
					}else{
						System.out.println("Error");
					}
				}else{
					System.out.println("nothing");
				}

				printWelcome();
			}catch (IOException e) {
				e.printStackTrace();
			}

		}catch(java.net.ConnectException ce){
			System.out.println("La connexion a échouée.\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static void printWelcome()
	{
		System.out.println("--------");
		System.out.println("Bienvenue !");
		System.out.println("--------");
		System.out.println("Client POP3 : Par Valentin Berger, Léa Chemoul, Philippine Cluniat, Elie Ensuque");
		System.out.println("--------");
	}


	private boolean send(String message) {
		PrintStream out_data = new PrintStream(this.op);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd'/'MM'/'yyyy 'at' HH:mm:ss");
			out_data.print("From: Client " + this.name + "\r\n");
			out_data.print("To: POP3 Server \r\n");
			out_data.print("Date: " + sdf.format(new Date())  + "\r\n");
			out_data.print("\r\n\r\n");
			out_data.print(message + "\r\n");
			out_data.flush();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void sendMessage(@NotNull Command command){
		String message = command.toString();
		this.send(message);
	}

	public void sendMessage(@NotNull Command command, @Nullable String param){
		String message = command + " " + param;
		this.send(message);
	}

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}

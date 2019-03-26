package org.pineapple.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pineapple.*;
import org.pineapple.MailBox;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Observable;

public class Client extends Observable {

	private String address = "192.168.43.12";
	private String password = "mdp";
	private boolean connected = false;
	private String name;
	private OutputStream op;
	private org.pineapple.MailBox messageHandler;

	public Client(String name) {
		this.name = name;
		this.messageHandler = new org.pineapple.MailBox(name, password);
		System.out.println("Connexion...");
	}

	public void connect(){
		try{
			Socket con_serv = new Socket(InetAddress.getByName(address),110);
			printWelcome();

			try {
				//flux d'entrée
				InputStream inp = con_serv.getInputStream();
				//flux de sortie
				this.op = con_serv.getOutputStream();

				InputStreamReader indata = new InputStreamReader(inp, StandardCharsets.ISO_8859_1);
				BufferedReader br = new BufferedReader(indata);

				StringBuilder content = new StringBuilder();

				boolean isMessage = false;
				while(true){
					String line = br.readLine();

					if(line != null){
						System.out.println(line);
						content.append(line.replace("+OK", ""));
						if(line.contains("From:")){
							isMessage = true;
						}else if(isMessage){
							if(content.length() > 0 && content.toString().contains("*")){
								this.messageHandler.addMessages(Message.parse(content.toString()));
								isMessage = false;
							}
						} else if(!isMessage){
							content = new StringBuilder();
							this.handleMessage(line);
						}

						setChanged();
						notifyObservers();

					}
				}

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
			out_data.print(message + "\r\n");
			out_data.flush();
			System.out.println("Sending : " + message);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void sendMessage(@NotNull CommandPOP3 command){
		String message = command.toString();
		this.send(message);
	}

	public void sendMessage(@NotNull CommandPOP3 command, @Nullable String param){
		String message = command + " " + param;
		this.send(message);
	}

	public void sendAPOP(String name, String password){
		this.sendMessage(CommandPOP3.APOP, name + " " + password);
	}

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    private void handleMessage(@NotNull String message){
		CommandMessage commandMessage = new CommandMessage();
//		try{
//			commandMessage.parseMessage(message);
//			if(commandMessage.isOKMessage()){
//				System.out.println("handling ok message");
//				this.handleOKServerMessage(commandMessage);
//			}else if(commandMessage.isERRMessage()){
//				System.out.println("handling err message");
//				this.handleERRServerMessage(commandMessage);
//			}
//		}catch (Exception ex){
//			System.out.println("Wrong format of message");
//		}
		if(message.contains("+OK Server POP3 server ready")){

		}else if(message.contains("+OK maildrop locked and ready")){
			this.setConnected(true);
		}else if(message.contains(":")){

		} else if(message.contains("+OK")){
			String[] parts = message.split(" ");
			int number = Integer.parseInt(parts[1]);
			this.askAllMessages(number);
//			this.sendMessage(CommandPOP3.RETR, "1");
		}
	}

    public void handleOKServerMessage(@NotNull CommandMessage commandMessage){
        if(commandMessage.getCodeOK() == CodeOK.CodeEnum.OK_RETRIEVE){
        	this.messageHandler.addMessages(Message.parse(commandMessage.getParameters().get(0)));
        }else if(commandMessage.getCodeOK() == CodeOK.CodeEnum.OK_STAT){
			int number = Integer.parseInt(commandMessage.getParameters().get(0));
			this.askAllMessages(number);
        }else if(commandMessage.getCodeOK().equals( new CodeOK(CodeOK.CodeEnum.OK_MAILDROP_READY))){
            this.setConnected(true);
        }else if(commandMessage.getCodeOK().equals( new CodeOK(CodeOK.CodeEnum.OK_SERVER_READY))){
			System.out.println("TCP connection established");
        }
    }

	public void handleERRServerMessage(@NotNull CommandMessage commandMessage){
		System.out.println(commandMessage.getCodeERR().toString());
	}

    private void askAllMessages(int number){
		for (int i = 1; i <= number; i++) {
			this.sendMessage(CommandPOP3.RETR,String.valueOf(i));
		}
	}

	public MailBox getMessageHandler() {
		return messageHandler;
	}
}

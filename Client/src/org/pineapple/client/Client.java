package org.pineapple.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client{

	static byte data [];
	static String path;
	static boolean quitter = false;

	private String adress = "127.0.0.1";
	private boolean connected = false;


	public Client() {
		System.out.println("Connexion...");
		try{
			Socket con_serv = new Socket(InetAddress.getByName(adress),110);
			try {
				this.setConnected(true);

				//flux de sortie
				InputStream inp = con_serv.getInputStream();

				//flux d'entrée
				OutputStream op = con_serv.getOutputStream();


				DataInputStream indata = new DataInputStream(con_serv.getInputStream());
				PrintWriter printWriter = new PrintWriter(op);

				Scanner sc = new Scanner(System.in);

				printWelcome();

				System.out.println("Press 0 to close connexion");
				int i = sc.nextInt();
				if (i == 0) {
					quitter = true;
                    this.setConnected(false);
                    con_serv.close();
				}

				if(!quitter){
					// receive a string from the server
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


	private static boolean send(PrintStream out_data, File f) {
		if (f == null)
			throw new NullPointerException();

		FileInputStream in = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] raw = new byte[4096];
		int size;

		try {
			in = new FileInputStream(f);

			while ((size = in.read(raw)) >= 0)
				baos.write(raw, 0, size);

			baos.flush();
			baos.close();

			byte[] data = baos.toByteArray();

			//TODO change to POP3
			out_data.print("PUT "  + f.getName() + " HTTP/1.1\r\n");
			out_data.print("Content-Length: " + data.length + "\r\n");
			out_data.print("\r\n");
			out_data.write(data, 0, data.length);
			out_data.print("\r\n");


			out_data.flush();

			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
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

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}

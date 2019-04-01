package org.pineapple.clientsmtp;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Observable;

public class ConnectionHandler extends Observable {

    private String address = "127.0.0.1";
    private boolean connected = false;
    private String name;
    private OutputStream op;
    private InputStream inp;

    public ConnectionHandler(String name) {
        this.name = name;
        System.out.println("Connexion...");
    }

    public void connect() {
        try {
            Socket con_serv = new Socket(InetAddress.getByName(address), 25);
            printWelcome();

            //flux d'entrée
            this.inp = con_serv.getInputStream();
            //flux de sortie
            this.op = con_serv.getOutputStream();

            InputStreamReader indata = new InputStreamReader(inp, StandardCharsets.ISO_8859_1);
            BufferedReader br = new BufferedReader(indata);

            StringBuilder content = new StringBuilder();

            boolean isMessage = false;
            while (true) {
                String line = br.readLine();

                if (line != null) {
                    System.out.println(line);

                    setChanged();
                    notifyObservers();

                }
            }
        } catch (java.net.ConnectException ce) {
            System.out.println("La connexion a échouée.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printWelcome() {
        System.out.println("--------");
        System.out.println("Bienvenue !");
        System.out.println("--------");
        System.out.println("Client SMTP : Par Valentin Berger, Léa Chemoul, Philippine Cluniat, Elie Ensuque");
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

//    public void sendMessage(@NotNull CommandPOP3 command){
//        String message = command.toString();
//        this.send(message);
//    }
//
//    public void sendMessage(@NotNull CommandPOP3 command, @Nullable String param){
//        String message = command + " " + param;
//        this.send(message);
//    }


    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    private void handleMessage(@NotNull String message) {

    }
}

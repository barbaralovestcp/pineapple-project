package org.pineapple.clientsmtp;


import com.sun.istack.internal.NotNull;
import org.pineapple.clientsmtp.stateMachine.ContextClient;
import org.pineapple.clientsmtp.stateMachine.InputStateMachineClient;
import org.pineapple.clientsmtp.stateMachine.StateConnected;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Observable;

public class ClientSMTP extends Observable {

    private boolean isConnected;
    private String serverMessage;

    private String address = "127.0.0.1";
    @NotNull
    private String name;
    @NotNull
    private String domain;

    //CONNEXION
    private OutputStream op;
    private InputStream inp;
    private Socket con_serv;

    //STATES
    private ContextClient context;


    public ClientSMTP(String name, String domain) {
        this.name = name;
        this.domain = domain;
    }

    public void connect() {
        System.out.println("Connexion...");
        try {
            con_serv = new Socket(InetAddress.getByName(address), 25);
            printWelcome();

            //flux d'entrée
            this.inp = con_serv.getInputStream();
            //flux de sortie
            this.op = con_serv.getOutputStream();
            this.context = new ContextClient(name,domain,new StateConnected());

            InputStreamReader indata = new InputStreamReader(inp, StandardCharsets.ISO_8859_1);
            BufferedReader br = new BufferedReader(indata);

            while ( !context.isToQuit() ) {
                StringBuilder content = new StringBuilder();
                try {
                    String line;

                    line = br.readLine(); //Read single line, looping block the line
                    if(line != null){
                        String[] parts = line.split("[\u0003\u0001]");
                        line = parts[parts.length-1];
                        System.out.println("Received server message : " + line);
                        content.append(line);
                        this.serverMessage = content.toString();

                        if (content.length() > 0) {

                            //Check if server message is a valid code (250,220,550,354)
                            if (InputStateMachineClient.isValidCommand(content.toString())) {
                                context.handle(new InputStateMachineClient(content.toString()));
                                System.out.println(context.getStateToLog());

                                String messageToSend = context.popMessageToSend();
                                //If there's a message to send, send it
                                if (messageToSend != null && !messageToSend.equals("")) {
                                    System.out.println("Sending message \"" + messageToSend.replace("\n", "\n\t") + "\"");
                                    send(messageToSend);
                                }
                                //Quit if to quit.
                                else if (context.isToQuit()) {
                                    System.out.println("Connection closed with server");
                                    this.con_serv.close();
                                }
                            }
                            else {
                                System.out.println("Invalid message from server!");
                            }

                            setChanged();
                            notifyObservers();
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
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



    boolean isConnected() {
        return isConnected;
    }

    public String getServerMessage() {
        return serverMessage;
    }
}

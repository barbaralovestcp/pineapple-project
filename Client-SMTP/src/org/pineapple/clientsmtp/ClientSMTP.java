package org.pineapple.clientsmtp;


import com.sun.istack.internal.NotNull;
import org.pineapple.Message;
import org.pineapple.clientsmtp.stateMachine.*;
import org.pineapple.CodeOKSMTP;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    private Message message;
    private ArrayList<String> recipient;


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

            //TODO enlever
            this.setConnected(true);

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

                            //TODO debuger
                            if(this.serverMessage.contains(new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK_GREETING).toString(this.domain).replace("\n",""))) {
                                this.setConnected(true);
                            }
                            if (InputStateMachineClient.isValidCommand(content.toString())) {
                                context.handle(new InputStateMachineClient(content.toString()));

                                String messageToSend = context.popMessageToSend();
                                //If there's a message to send, send it
                                if (messageToSend != null && !messageToSend.equals("")) {
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
            System.out.println("\n Sending message \"" + message.replace("\n", "\n\t") + "\"");
            out_data.flush();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void initMailTransaction(Message message, ArrayList<String> recipient){
        this.context.setMessage(message);
        this.context.setRecipient(recipient);
        this.context.setRecipientIterator(0);
        if(context.getCurrentState() instanceof StateWaitingGreeting){
            String toSend = "MAIL FROM:" + " " + name + "@" + domain;
            this.send(toSend);
            this.context.setState(new StateWaitingMailFromAnswer());
        }
    }


    boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getServerMessage() {
        return serverMessage;
    }
}

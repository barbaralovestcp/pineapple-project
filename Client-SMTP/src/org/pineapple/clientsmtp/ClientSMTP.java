package org.pineapple.clientsmtp;


import com.sun.istack.internal.NotNull;
import org.pineapple.CodeOKSMTP;
import org.pineapple.Message;
import org.pineapple.clientsmtp.stateMachine.ContextClient;
import org.pineapple.clientsmtp.stateMachine.InputStateMachineClient;
import org.pineapple.clientsmtp.stateMachine.StateConnected;
import org.pineapple.clientsmtp.stateMachine.StateWaitingMailFromAnswer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

public class ClientSMTP extends Observable {

    private boolean isConnected;
    private String serverMessage;
    private String logMessage;

    private String address = "192.168.43.16";
    @NotNull
    private String name;
    @NotNull
    private String domain;

    //CONNEXION
    private OutputStream op;
    private InputStream inp;
    private Socket con_serv;

    InputStreamReader indata;
    BufferedReader br;

    //STATES
    private ContextClient context;


    public ClientSMTP(String name, String domain) {
        this.name = name;
        this.domain = domain;
    }

    public void connect() {
        System.out.println("Connexion...");
        try {
            con_serv = new Socket(InetAddress.getByName(address), 1025);
            printWelcome();

            //flux d'entrée
            this.inp = con_serv.getInputStream();
            //flux de sortie
            this.op = con_serv.getOutputStream();
            this.context = new ContextClient(name, domain, new StateConnected());

            //TODO : Je ne suis pas sur si à chaque loop il devrait être ré-init ou non. Je les init qu'une seul fois pour l'instant
            this.indata = new InputStreamReader(inp, StandardCharsets.ISO_8859_1);
            this.br = new BufferedReader(indata);


            //TODO enlever
            this.setConnected(true);

            stateMachineLoop();

        } catch (java.net.ConnectException ce) {
            System.out.println("La connexion a échouée.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stateMachineLoop() {

        do {
            StringBuilder content = new StringBuilder();
            try {
                String line;

                //Read the next line sent by the server
                line = br.readLine(); //Read single line, looping block the line
                if (line != null) {

                    //Parse and read the line.
                    String[] parts = line.split("[\u0003\u0001]");
                    line = parts[parts.length - 1];
                    System.out.println("Received server message : " + line);
                    content.append(line);
                    this.serverMessage = content.toString();

                    //If the line is not empty
                    if (content.length() > 0) {

                        //TODO debuger
                        if (this.serverMessage.contains(new CodeOKSMTP(CodeOKSMTP.CodeEnum.OK_GREETING).toString(this.domain).replace("\n", ""))) {
                            this.setConnected(true);
                        }
                        if (InputStateMachineClient.isValidCommand(content.toString())) {
                            context.handle(new InputStateMachineClient(content.toString()));

                            String messageToSend = context.popMessageToSend();
                            String messageToLog = context.logStateRecipient();
                            //If there's a message to send, send it
                            if (messageToSend != null && !messageToSend.equals("")) {
                                send(messageToSend);
                            }
                            //If there's a message to log, log it
                            if (messageToLog != null && !messageToLog.equals("")) {
                                this.logMessage = messageToLog;
                            }
                            //Quit if to quit.
                            else if (context.isToQuit()) {
                                System.out.println("Connection closed with server");
                                if (this.con_serv.isConnected()) {
                                    this.con_serv.close();
                                    break;
                                }
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
        } while (!(context.getCurrentState() instanceof StateWaitingMailFromAnswer) && this.con_serv.isConnected() && !context.isToQuit());
    }

    private static void printWelcome() {
        System.out.println("--------");
        System.out.println("Bienvenue !");
        System.out.println("--------");
        System.out.println("Client SMTP : Par Valentin Berger, Léa Chemoul, Philippine Cluniat, Elie Ensuque");
        System.out.println("--------");
    }

    public boolean send(String message) {
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

    public void initMailTransaction(Message message, ArrayList<String> recipient) {

        if (context.getCurrentState() instanceof StateWaitingMailFromAnswer) {

            //Initialise the mail informations
            this.context.setMessage(message);
            this.context.setRecipient(recipient);
            this.context.setRecipientIterator(0);
            this.context.setValidIRecipient(0);
            this.context.getStateRecipient().clear();

            //Send message to server
            String toSend = "MAIL FROM:" + " " + name + "@" + domain;
            this.send(toSend);

            //Wait for a server answer and resume the stateMachine automated loop
            stateMachineLoop();
        } else {
            System.out.println("DEBUG : Not in StateWaitingMailFromAnswer, can't take client input!");
        }
    }

    public void quit() {
        if (context.getCurrentState() instanceof StateWaitingMailFromAnswer) {

            //Send message to server
            String toSend = "QUIT";
            this.send(toSend);

            //Wait for a server answer and resume the stateMachine automated loop
            stateMachineLoop();
        } else {
            System.out.println("DEBUG : Not in StateWaitingMailFromAnswer, can't take client input!");
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

    public String getLogMessage() {
        return logMessage;
    }
}

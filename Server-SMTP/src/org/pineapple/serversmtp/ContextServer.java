package org.pineapple.serversmtp;

import org.pineapple.MailBox;
import org.pineapple.Message;
import org.pineapple.UserManager;
import org.pineapple.stateMachine.Context;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ContextServer extends Context {

    private String domain;
    private String mailFrom;
    private ArrayList<String> mailRcpt = new ArrayList<>();
    private StringBuilder mailBody = new StringBuilder();



    public ContextServer() {
        super(new StateListening());
        this.domain = "pine.apple";
    }


    public void clearMailData() {
        mailBody = new StringBuilder();
        mailFrom = "";
        mailRcpt.clear();
    }

    public void addMailRcpt(String mailRcpt) {
        this.mailRcpt.add(mailRcpt);
    }

    public void saveCurrentMessage() {

        if (mailFrom.equals("") || mailRcpt.isEmpty() || mailBody.length() == 0) {
            System.out.println("ERREUR : Segment nécessaire d'un message incomplet :");
            System.out.println(mailFrom);
            System.out.println(mailRcpt.toString());
            System.out.println(mailBody.toString());
            return;
        }

        //Build the Mail !
        Message mail = new Message();

        mail.setSender(mailFrom);

        //build rcpt
        StringBuilder rcpts = new StringBuilder();
        for (String rcpt : mailRcpt) {
            rcpts.append("<").append(rcpt).append("> ");
        }
        mail.setReceiver(rcpts.toString());
        mail.setMessage(mailBody.toString());

        //Add the message to the mailbox of each recipient
        for (String rcpt : mailRcpt) {
            String encryptedPassword = UserManager.getEncryptedPassword(rcpt);
    
            MailBox recipient;
            
            // If recipient does not exist, create a mailbox
            if (encryptedPassword == null)
                recipient = new MailBox(rcpt, "", false);
            else
                recipient = new MailBox(rcpt, encryptedPassword, true);
            
            recipient.addMessages(mail);
        }
    }

    public String getDomain() {
        return this.domain;
    }


    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public ArrayList<String> getMailRcpt() {
        return mailRcpt;
    }



    public StringBuilder getMailBody() {
        return mailBody;
    }

}

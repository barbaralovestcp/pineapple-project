package org.pineapple.server;

import org.pineapple.Message;

import java.util.ArrayList;

public class MailBox {
    private String name;
    private String password;
    private ArrayList<Message> messages;

    public MailBox(String name, String password, ArrayList<Message> messages) {
        this.name = name;
        this.password = password;
        this.messages = messages;
    }

   //GETTERS

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public int getMessagesNumber() {
        return messages.size();
    }

    public int getTotalSize() {
        int size = 0;
        for (Message message: messages) {
            size += message.getMessageSize();
        }
        return size;
    }
}

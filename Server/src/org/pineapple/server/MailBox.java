package org.pineapple.server;

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
}

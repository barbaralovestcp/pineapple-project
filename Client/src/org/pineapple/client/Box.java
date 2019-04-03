package org.pineapple.client;

import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.pineapple.Message;

public class Box {

    private String name;
    private ObservableList<Message> messages;

    public Box(String name, ObservableList<Message> messages) {
        this.name = name;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public ObservableList<Message> getMessages() {
        return messages;
    }

    public Message remove(Message m) {
        messages.remove(m);
        return m;
    }

    public void add(Message m) {
        messages.add(m);
    }

    public ObservableList<Message> addAll(ObservableList<Message> m) {
        for (Message item : m) {
            messages.add(item);
        }
        return messages;
    }
    
    public void setMessages(@NotNull ObservableList<Message> messages) {
        this.messages = messages;
    }
    
    public String toString() {
        return this.name;
    }
}

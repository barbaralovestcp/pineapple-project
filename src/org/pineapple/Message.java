package org.pineapple;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Message {
    
    public static final String SEPARATOR = "******";
    public static final String SEPARATOR_REGEX = "(?m)^\\*{6}\\s*$";

    private String sender;
    private String receiver;
    private String subject;
    private String date;
    private String messageId;
    private String message;
    private boolean isDeleted;
    
    public Message() {
        this.sender = "Unknown";
        this.receiver = "Unknown";
        this.subject = "Unknown";
        this.date = "Unknown";
        this.messageId = "Unknown";
        this.message = "";
        this.isDeleted = false;
    }

    public String buildMessage() {
        return getInfos() +
                "Message-ID: " + getMessageId() + "\r\n" +
                "\r\n" +
                getMessage() +"\r\n";
    }
    
    /**
     * Parse a message from string.
     * @param rawMessage The message to parse.
     * @return Return the representation of the string as a message.
     */
    public static Message parse(@NotNull String rawMessage) {
        String[] lines = rawMessage.split("\\r\\n");
        
        if (lines.length == 1)
            lines = rawMessage.split("\\n");
        
        Message message = new Message();
        
        for (String line : lines) {
            // Split the line according to ':'
            String[] parts = line.split("\\s*:\\s*");
            
            // If the line could not be split, then it is the body
            if (parts.length == 1)
                message.setMessage(message.getMessage() + line + ((message.getMessage() + line).length() > 0 ? "\r\n" : ""));
            else {
                if (parts[0].toLowerCase().equals("from"))
                    message.setSender(parts[1]);
                else if (parts[0].toLowerCase().equals("to"))
                    message.setReceiver(parts[1]);
                else if (parts[0].toLowerCase().equals("date")) {
                    String[] dates = new String[parts.length - 1];
                    System.arraycopy(parts, 1, dates, 0, parts.length - 1);
                    message.setDate(String.join(":", dates));
                }
                else if (parts[0].toLowerCase().equals("message-id"))
                    message.setMessageId(parts[1]);
                else if (parts[0].toLowerCase().equals("subject"))
                    message.setSubject(parts[1]);
                // If it does not match any conditions above, then it's the body where there is one or multiple ':'.
                else
                    message.setMessage(message.getMessage() + line + "\r\n");
            }
        }
        
        // Delete the last "\r\n"
        if (message.getMessage().contains("\r\n"))
            message.setMessage(message.getMessage().replaceFirst("\\r\\n$", ""));
        
        return message;
    }

    //GETTERS
    
    public String getSender() {
        return sender;
    }
    
    public String getReceiver() {
        return receiver;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public String getDate() {
        return date;
    }
    
    public String getMessageId() {
        return messageId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public int getMessageSize() {
        return this.buildMessage().getBytes().length;
    }
    
    public boolean getIsDeleted() {
        return isDeleted;
    }
    
    public String getInfos(){
        return "From: " + getSender() + "\r\n" +
                "To: " + getReceiver() + "\r\n" +
                "Subject: " + getSubject() + "\r\n" +
                "Date: " + getDate() + "\r\n";
    }

    //SETTERS

    public Message setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public Message setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public Message setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public Message setDate(String date) {
        this.date = date;
        return this;
    }

    public Message setMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public Message setDeleted(Boolean deleted) {
        isDeleted = deleted;
        return this;
    }
    
    /* OVERRIDES */
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message1 = (Message) o;
        return getIsDeleted() == message1.getIsDeleted() &&
                Objects.equals(getSender(), message1.getSender()) &&
                Objects.equals(getReceiver(), message1.getReceiver()) &&
                Objects.equals(getSubject(), message1.getSubject()) &&
                Objects.equals(getDate(), message1.getDate()) &&
                Objects.equals(getMessageId(), message1.getMessageId()) &&
                Objects.equals(getMessage(), message1.getMessage());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getSender(), getReceiver(), getSubject(), getDate(), getMessageId(), getMessage(), getIsDeleted());
    }
    
    @Override
    public String toString() {
        return buildMessage();
    }
}

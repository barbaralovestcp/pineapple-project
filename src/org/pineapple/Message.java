package org.pineapple;

public class Message {

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

    @Override
    public String toString() {
        return buildMessage();
    }
}

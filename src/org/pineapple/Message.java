package org.pineapple;

public class Message {

    private String sender;
    private String receiver;
    private String subject;
    private String date;
    private String messageId;
    private String message;
    private Boolean isDeleted;

    public Message(String sender, String receiver, String subject, String date, String messageId, String message, Boolean isDeleted) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.date = date;
        this.messageId = messageId;
        this.message = message;
        this.isDeleted = isDeleted;
    }

    public Message(){
        this.sender = "Unknown";
        this.receiver = "Unknown";
        this.subject = "Unknown";
        this.date = "Unknown";
        this.messageId = "Unknown";
        this.message = "";
        this.isDeleted = false;
    }

    public String buildMessage() {
        return "From: " + this.sender + "/n" +
                "To: " + this.receiver + "/n" +
                "Subject: " + this.subject + "/n" +
                "Date: " + this.date + "/n" +
                "Message-ID: " + this.messageId + "/n" +
                "/n" +
                this.message +"/n";
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

    public Boolean getIsDeletec() {
        return isDeleted;
    }

    public String getInfos(){
        return "From: " + this.sender + "/n" +
                "To: " + this.receiver + "/n" +
                "Subject: " + this.subject + "/n" +
                "Date: " + this.date + "/n";
    }

    //SETTERS


    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public int getMessageSize() {
        //TODO: return size of a message (octets) for list command
        return 0;
    }

    @Override
    public String toString() {
        return this.getSubject();
    }
}

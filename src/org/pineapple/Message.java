package org.pineapple;

public class Message {

    private String sender;
    private String receiver;
    private String subject;
    private String date;
    private String messageId;
    private String message;

    public Message(String sender, String receiver, String subject, String date, String messageId, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.date = date;
        this.messageId = messageId;
        this.message = message;
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
}
